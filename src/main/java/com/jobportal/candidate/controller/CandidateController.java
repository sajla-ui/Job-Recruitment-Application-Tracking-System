package com.jobportal.candidate.controller;

import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.service.CandidateService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/register")
    public ResponseEntity<Candidate> register(@Valid @RequestBody Candidate candidate) {
        Candidate registered = candidateService.registerCandidate(candidate);
        registered.setPassword(null); // Never return password
        return new ResponseEntity<>(registered, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Candidate> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Candidate candidate = candidateService.loginCandidate(loginRequest.getEmail(), loginRequest.getPassword());
        
        // Simple Session-Based Auth
        session.setAttribute("candidateId", candidate.getId());
        session.setAttribute("role", "CANDIDATE");
        
        candidate.setPassword(null);
        return ResponseEntity.ok(candidate);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<Candidate> getCurrentUser(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("candidateId");
        if (candidateId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Candidate candidate = candidateService.getCandidateById(candidateId);
        candidate.setPassword(null);
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateProfile(@PathVariable Long id, @RequestBody Candidate candidate, HttpSession session) {
        Long loggedInId = (Long) session.getAttribute("candidateId");
        if (loggedInId == null || !loggedInId.equals(id)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Candidate updated = candidateService.updateProfile(id, candidate);
        updated.setPassword(null);
        return ResponseEntity.ok(updated);
    }
    
    // DTO for Login
    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
