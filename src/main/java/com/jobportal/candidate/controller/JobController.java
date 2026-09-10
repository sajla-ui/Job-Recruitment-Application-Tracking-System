package com.jobportal.candidate.controller;

import com.jobportal.candidate.dto.JobDTO;
import com.jobportal.candidate.service.JobIntegrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for Candidate Module to interact with Jobs.
 * Uses the integration interface.
 */
@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobIntegrationService jobIntegrationService;

    public JobController(JobIntegrationService jobIntegrationService) {
        this.jobIntegrationService = jobIntegrationService;
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        
        if (keyword != null || location != null) {
            return ResponseEntity.ok(jobIntegrationService.searchJobs(keyword, location));
        }
        return ResponseEntity.ok(jobIntegrationService.getAllAvailableJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        JobDTO job = jobIntegrationService.getJobById(id);
        if (job != null) {
            return ResponseEntity.ok(job);
        }
        return ResponseEntity.notFound().build();
    }
}
