package com.jobportal.application.controller;

import com.jobportal.application.model.Application;
import com.jobportal.application.service.ApplicationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // =========================================================
    // CANDIDATE APPLIES FOR A JOB
    // =========================================================

    @PostMapping
    public ResponseEntity<Application> createApplication(
            @RequestParam Long candidateId,
            @RequestParam Long jobId,
            @RequestParam(required = false) String coverLetter) {

        Application application =
                applicationService.createApplication(
                        candidateId,
                        jobId,
                        coverLetter
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(application);
    }

    // =========================================================
    // GET ONE APPLICATION
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplication(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                applicationService.getApplicationById(id)
        );
    }

    // =========================================================
    // GET CANDIDATE APPLICATIONS
    // =========================================================

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<Application>> getCandidateApplications(
            @PathVariable Long candidateId) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByCandidate(candidateId)
        );
    }

    // =========================================================
    // GET ALL APPLICATIONS FOR RECRUITER
    // =========================================================

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Application>> getRecruiterApplications(
            @PathVariable Long recruiterId) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByRecruiter(recruiterId)
        );
    }

    // =========================================================
    // GET APPLICATIONS FOR A PARTICULAR JOB
    // =========================================================

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam Long recruiterId) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByJob(
                        jobId,
                        recruiterId
                )
        );
    }

    // =========================================================
    // UPDATE APPLICATION STATUS
    // =========================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id,
            @RequestParam Long recruiterId,
            @RequestParam String status) {

        Application application =
                applicationService.updateStatus(
                        id,
                        recruiterId,
                        status
                );

        return ResponseEntity.ok(application);
    }
}