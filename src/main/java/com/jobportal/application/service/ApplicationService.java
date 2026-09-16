package com.jobportal.application.service;

import com.jobportal.application.model.Application;

import java.util.List;

public interface ApplicationService {

    // Candidate applies for a job
    Application createApplication(
            Long candidateId,
            Long jobId,
            String coverLetter
    );

    // Get one application
    Application getApplicationById(Long applicationId);

    // Get applications submitted by a candidate
    List<Application> getApplicationsByCandidate(Long candidateId);

    // Get all applications received by a recruiter
    List<Application> getApplicationsByRecruiter(Long recruiterId);

    // Get applications for one particular job
    List<Application> getApplicationsByJob(
            Long jobId,
            Long recruiterId
    );

    // Recruiter changes application status
    Application updateStatus(
            Long applicationId,
            Long recruiterId,
            String status
    );
}