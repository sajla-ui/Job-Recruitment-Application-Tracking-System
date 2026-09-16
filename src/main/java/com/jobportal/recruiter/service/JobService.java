package com.jobportal.recruiter.service;

import com.jobportal.recruiter.model.Job;

import java.util.List;

public interface JobService {

    // Create a new job
    Job createJob(Long recruiterId, Job job);

    // Get one job
    Job getJobById(Long jobId);

    // Get all jobs
    List<Job> getAllJobs();

    // Get jobs posted by one recruiter
    List<Job> getJobsByRecruiter(Long recruiterId);

    // Update a job
    Job updateJob(Long recruiterId, Long jobId, Job updatedJob);

    // Delete a job
    void deleteJob(Long recruiterId, Long jobId);

    // Search jobs
    List<Job> searchJobs(String keyword, String location);
}