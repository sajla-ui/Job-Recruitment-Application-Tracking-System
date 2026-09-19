package com.jobportal.candidate.service;

import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.model.CandidateSkill;
import com.jobportal.candidate.model.Education;
import com.jobportal.candidate.model.Experience;
import com.jobportal.candidate.model.Resume;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Demonstrating OOP Concept: Abstraction.
 * This interface hides the complex business logic implementation from the controllers.
 */
public interface CandidateService {
    
    // Auth
    Candidate registerCandidate(Candidate candidate);
    Candidate loginCandidate(String email, String plainTextPassword);

    // Profile
    Candidate getCandidateById(Long id);
    Candidate updateProfile(Long id, Candidate updatedData);

    
    // Skills
Candidate addSkill(Long candidateId, String skillName, String skillLevel);
List<CandidateSkill> getSkills(Long candidateId);
void removeSkill(Long candidateId, Long skillId);

    // Education
    Education addEducation(Long candidateId, Education education);
    List<Education> getEducation(Long candidateId);
    void deleteEducation(Long candidateId, Long educationId);

    // Experience
    Experience addExperience(Long candidateId, Experience experience);
    void deleteExperience(Long candidateId, Long experienceId);
    List<Experience> getExperiences(Long candidateId);

    // Resume
    Resume uploadResume(Long candidateId, MultipartFile file);
    void deleteResume(Long candidateId, Long resumeId);
    // Resume 

List<Resume> getResumes(Long candidateId);
}
