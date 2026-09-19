package com.jobportal.candidate.service.impl;

import com.jobportal.candidate.model.*;
import com.jobportal.candidate.repository.*;
import com.jobportal.candidate.service.CandidateService;
import com.jobportal.shared.exception.DuplicateResourceException;
import com.jobportal.shared.exception.ResourceNotFoundException;
import com.jobportal.shared.exception.UnauthorizedException;
import com.jobportal.shared.repository.UserRepository;
import com.jobportal.shared.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.jobportal.candidate.model.Resume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

/**
 * Demonstrating OOP Concept: Polymorphism (Implementing an Interface).
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;
    private final CandidateSkillRepository candidateSkillRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final ResumeRepository resumeRepository;

    @Value("${app.upload.dir:./uploads/resumes/}")
    private String uploadDir;

    // Demonstrating OOP Concept: Constructors (Dependency Injection)
    public CandidateServiceImpl(UserRepository userRepository, 
                                CandidateRepository candidateRepository,
                                SkillRepository skillRepository,
                                CandidateSkillRepository candidateSkillRepository,
                                EducationRepository educationRepository,
                                ExperienceRepository experienceRepository,
                                ResumeRepository resumeRepository) {
        this.userRepository = userRepository;
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
        this.candidateSkillRepository = candidateSkillRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
        this.resumeRepository = resumeRepository;
    }

    @Override
    @Transactional
    public Candidate registerCandidate(Candidate candidate) {
        if (userRepository.existsByEmail(candidate.getEmail())) {
            throw new DuplicateResourceException("Email is already registered.");
        }
        candidate.setPassword(PasswordUtil.hashPassword(candidate.getPassword()));
        candidate.setRole("CANDIDATE");
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate loginCandidate(String email, String plainTextPassword) {
        Candidate candidate = candidateRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password."));
        
        if (!PasswordUtil.checkPassword(plainTextPassword, candidate.getPassword())) {
            throw new UnauthorizedException("Invalid email or password.");
        }
        return candidate;
    }

    @Override
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + id));
    }

    @Override
    @Transactional
    public Candidate updateProfile(Long id, Candidate updatedData) {
        Candidate existing = getCandidateById(id);
        existing.setName(updatedData.getName());
        existing.setPhone(updatedData.getPhone());
        existing.setLocation(updatedData.getLocation());
        existing.setProfileSummary(updatedData.getProfileSummary());
        return candidateRepository.save(existing);
    }

    @Override
    @Transactional
    public Candidate addSkill(Long candidateId, String skillName, String skillLevel) {
        Candidate candidate = getCandidateById(candidateId);
        
        Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                .orElseGet(() -> skillRepository.save(new Skill(skillName)));

        CandidateSkill candidateSkill = new CandidateSkill(candidate, skill, skillLevel);
        candidateSkillRepository.save(candidateSkill);
        
        // Refresh candidate to reflect new skill in collection
        return getCandidateById(candidateId);
    }

    @Override
    @Transactional
    public void removeSkill(Long candidateId, Long skillId) {
        CandidateSkillId id = new CandidateSkillId(candidateId, skillId);
        candidateSkillRepository.deleteById(id);
    }

    @Override
    public Education addEducation(Long candidateId, Education education) {
        Candidate candidate = getCandidateById(candidateId);
        education.setCandidate(candidate);
        return educationRepository.save(education);
    }

    @Override
    public void deleteEducation(Long candidateId, Long educationId) {
        Education edu = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education not found"));
        if (!edu.getCandidate().getId().equals(candidateId)) {
            throw new UnauthorizedException("Cannot delete someone else's education");
        }
        educationRepository.delete(edu);
    }

    @Override
    public Experience addExperience(Long candidateId, Experience experience) {
        Candidate candidate = getCandidateById(candidateId);
        experience.setCandidate(candidate);
        return experienceRepository.save(experience);
    }

    @Override
    public void deleteExperience(Long candidateId, Long experienceId) {
        Experience exp = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        if (!exp.getCandidate().getId().equals(candidateId)) {
            throw new UnauthorizedException("Cannot delete someone else's experience");
        }
        experienceRepository.delete(exp);
    }

    @Override
    @Transactional
    public Resume uploadResume(Long candidateId, MultipartFile file) {
        Candidate candidate = getCandidateById(candidateId);
        
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                    ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFileName);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Resume resume = new Resume(candidate, originalFilename, file.getContentType(), filePath.toString());
            return resumeRepository.save(resume);

        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }

    @Override
    @Transactional
    public void deleteResume(Long candidateId, Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        if (!resume.getCandidate().getId().equals(candidateId)) {
            throw new UnauthorizedException("Cannot delete someone else's resume");
        }
        
        try {
            Files.deleteIfExists(Paths.get(resume.getFilePath()));
        } catch (IOException e) {
            System.err.println("Failed to delete physical file: " + e.getMessage());
        }
        
        resumeRepository.delete(resume);
    }
    @Override
@Transactional(readOnly = true)
public List<Resume> getResumes(Long candidateId) {
    getCandidateById(candidateId);
    return resumeRepository.findByCandidateId(candidateId);
}
}
