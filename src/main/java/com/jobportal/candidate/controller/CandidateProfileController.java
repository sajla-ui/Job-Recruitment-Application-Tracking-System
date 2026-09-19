package com.jobportal.candidate.controller;

import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.model.Education;
import com.jobportal.candidate.model.Experience;
import com.jobportal.candidate.model.Resume;
import com.jobportal.candidate.service.CandidateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/candidates/{candidateId}")
public class CandidateProfileController {

    private final CandidateService candidateService;

    public CandidateProfileController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    private boolean isAuthorized(Long pathId, HttpSession session) {
        Long loggedInId = (Long) session.getAttribute("candidateId");
        return loggedInId != null && loggedInId.equals(pathId);
    }

    // --- SKILLS ---
    @PostMapping("/skills")
    public ResponseEntity<?> addSkill(@PathVariable Long candidateId, @RequestBody Map<String, String> payload, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        
        String skillName = payload.get("skillName");
        String skillLevel = payload.get("skillLevel");
        Candidate updated = candidateService.addSkill(candidateId, skillName, skillLevel);
        return ResponseEntity.ok(updated.getCandidateSkills());
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<?> removeSkill(@PathVariable Long candidateId, @PathVariable Long skillId, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        candidateService.removeSkill(candidateId, skillId);
        return ResponseEntity.ok().build();
    }

    // --- EDUCATION ---
    @PostMapping("/education")
    public ResponseEntity<Education> addEducation(@PathVariable Long candidateId, @RequestBody Education education, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.addEducation(candidateId, education));
    }

    @DeleteMapping("/education/{educationId}")
    public ResponseEntity<?> deleteEducation(@PathVariable Long candidateId, @PathVariable Long educationId, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        candidateService.deleteEducation(candidateId, educationId);
        return ResponseEntity.ok().build();
    }

    // --- EXPERIENCE ---
    @PostMapping("/experience")
    public ResponseEntity<Experience> addExperience(@PathVariable Long candidateId, @RequestBody Experience experience, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.addExperience(candidateId, experience));
    }

    @DeleteMapping("/experience/{experienceId}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long candidateId, @PathVariable Long experienceId, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        candidateService.deleteExperience(candidateId, experienceId);
        return ResponseEntity.ok().build();
    }
@GetMapping("/resume")
public ResponseEntity<List<Resume>> getResumes(
        @PathVariable Long candidateId,
        HttpSession session) {

    if (!isAuthorized(candidateId, session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(candidateService.getResumes(candidateId));
}
    // --- RESUME ---
    @PostMapping("/resume")
    public ResponseEntity<Resume> uploadResume(@PathVariable Long candidateId, @RequestParam("file") MultipartFile file, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.uploadResume(candidateId, file));
    }

    @DeleteMapping("/resume/{resumeId}")
    public ResponseEntity<?> deleteResume(@PathVariable Long candidateId, @PathVariable Long resumeId, HttpSession session) {
        if (!isAuthorized(candidateId, session)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        candidateService.deleteResume(candidateId, resumeId);
        return ResponseEntity.ok().build();
    }
}
