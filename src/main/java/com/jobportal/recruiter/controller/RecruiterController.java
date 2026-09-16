package com.jobportal.recruiter.controller;

import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.service.RecruiterService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<Recruiter> register(
            @RequestBody Recruiter recruiter) {

        Recruiter savedRecruiter =
                recruiterService.registerRecruiter(recruiter);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRecruiter);
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<Recruiter> login(
            @RequestParam String email,
            @RequestParam String password) {

        Recruiter recruiter =
                recruiterService.loginRecruiter(
                        email,
                        password
                );

        return ResponseEntity.ok(recruiter);
    }

    // =========================
    // GET PROFILE
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getProfile(
            @PathVariable Long id) {

        Recruiter recruiter =
                recruiterService.getRecruiterById(id);

        return ResponseEntity.ok(recruiter);
    }

    // =========================
    // UPDATE PROFILE
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateProfile(
            @PathVariable Long id,
            @RequestBody Recruiter updatedData) {

        Recruiter updatedRecruiter =
                recruiterService.updateProfile(
                        id,
                        updatedData
                );

        return ResponseEntity.ok(updatedRecruiter);
    }
}