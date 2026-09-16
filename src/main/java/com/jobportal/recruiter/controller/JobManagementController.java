package com.jobportal.recruiter.controller;

import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.service.JobService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter/jobs")
@CrossOrigin
public class JobManagementController {

    private final JobService jobService;

    public JobManagementController(JobService jobService) {
        this.jobService = jobService;
    }

    // =========================
    // POST A JOB
    // =========================

    @PostMapping
    public ResponseEntity<Job> createJob(
            @RequestParam Long recruiterId,
            @RequestBody Job job) {

        Job createdJob = jobService.createJob(recruiterId, job);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdJob);
    }

    // =========================
    // SEARCH JOBS
    // IMPORTANT: Keep this BEFORE /{id}
    // =========================

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {

        return ResponseEntity.ok(
                jobService.searchJobs(keyword, location)
        );
    }

    // =========================
    // GET ALL JOBS
    // =========================

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {

        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    // =========================
    // GET RECRUITER'S JOBS
    // IMPORTANT: Keep this BEFORE /{id}
    // =========================

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getRecruiterJobs(
            @PathVariable Long recruiterId) {

        return ResponseEntity.ok(
                jobService.getJobsByRecruiter(recruiterId)
        );
    }

    // =========================
    // GET ONE JOB
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    // =========================
    // UPDATE JOB
    // =========================

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(
            @PathVariable Long id,
            @RequestParam Long recruiterId,
            @RequestBody Job updatedJob) {

        Job job = jobService.updateJob(
                recruiterId,
                id,
                updatedJob
        );

        return ResponseEntity.ok(job);
    }

    // =========================
    // DELETE JOB
    // =========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id,
            @RequestParam Long recruiterId) {

        jobService.deleteJob(
                recruiterId,
                id
        );

        return ResponseEntity.noContent().build();
    }
}