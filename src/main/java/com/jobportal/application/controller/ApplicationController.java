package com.jobportal.application.controller;

import com.jobportal.application.model.Application;
import com.jobportal.application.service.ApplicationService;
import com.jobportal.candidate.model.Resume;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    // =========================================================
// UPDATE FINAL RESULT AND REMARKS
// =========================================================

@PutMapping("/{id}/result")
public ResponseEntity<Application> updateResult(
        @PathVariable Long id,
        @RequestParam Long recruiterId,
        @RequestParam String status,
        @RequestParam(required = false) String resultRemarks) {

    Application application =
            applicationService.updateResult(
                    id,
                    recruiterId,
                    status,
                    resultRemarks
            );

    return ResponseEntity.ok(application);
}
    // =========================================================
// RECRUITER VIEWS APPLICANT RESUME
// =========================================================

@GetMapping("/{applicationId}/resume")
public ResponseEntity<Resource> viewApplicantResume(
        @PathVariable Long applicationId,
        @RequestParam Long recruiterId) {

    Resume resume =
            applicationService.getCandidateResumeForRecruiter(
                    applicationId,
                    recruiterId
            );

    try {
        Path path = Paths.get(resume.getFilePath());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (resume.getFileType() != null) {
            try {
                mediaType = MediaType.parseMediaType(
                        resume.getFileType()
                );
            } catch (Exception ignored) {
                // Keep default media type
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                resume.getFileName() +
                                "\""
                )
                .body(resource);

    } catch (Exception e) {
        return ResponseEntity.internalServerError().build();
    }
}
}