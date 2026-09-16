package com.jobportal.application.service.impl;

import com.jobportal.application.model.Application;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.service.ApplicationService;
import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.repository.CandidateRepository;
import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.repository.JobRepository;
import com.jobportal.recruiter.repository.RecruiterRepository;
import com.jobportal.shared.exception.DuplicateResourceException;
import com.jobportal.shared.exception.ResourceNotFoundException;
import com.jobportal.shared.exception.UnauthorizedException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;

    public ApplicationServiceImpl(
            ApplicationRepository applicationRepository,
            CandidateRepository candidateRepository,
            JobRepository jobRepository,
            RecruiterRepository recruiterRepository) {

        this.applicationRepository = applicationRepository;
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    // =========================================================
    // CANDIDATE APPLIES FOR A JOB
    // =========================================================

    @Override
    @Transactional
    public Application createApplication(
            Long candidateId,
            Long jobId,
            String coverLetter) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Candidate not found with id " + candidateId));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id " + jobId));

        // Prevent duplicate applications
        if (applicationRepository.existsByCandidateAndJob(candidate, job)) {
            throw new DuplicateResourceException(
                    "You have already applied for this job.");
        }

        Application application =
                new Application(candidate, job, coverLetter);

        return applicationRepository.save(application);
    }

    // =========================================================
    // GET ONE APPLICATION
    // =========================================================

    @Override
    public Application getApplicationById(Long applicationId) {

        return applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found with id " + applicationId));
    }

    // =========================================================
    // GET APPLICATIONS OF A CANDIDATE
    // =========================================================

    @Override
    public List<Application> getApplicationsByCandidate(
            Long candidateId) {

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Candidate not found with id " + candidateId));

        return applicationRepository.findByCandidate(candidate);
    }

    // =========================================================
    // GET ALL APPLICATIONS FOR A RECRUITER
    // =========================================================

    @Override
    public List<Application> getApplicationsByRecruiter(
            Long recruiterId) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recruiter not found with id " + recruiterId));

        return applicationRepository.findByJobRecruiter(recruiter);
    }

    // =========================================================
    // GET APPLICATIONS FOR ONE JOB
    // =========================================================

    @Override
    public List<Application> getApplicationsByJob(
            Long jobId,
            Long recruiterId) {

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id " + jobId));

        // Make sure this job belongs to the recruiter
        if (!job.getRecruiter().getId().equals(recruiterId)) {
            throw new UnauthorizedException(
                    "You cannot view applications for someone else's job.");
        }

        return applicationRepository.findByJob(job);
    }

    // =========================================================
    // RECRUITER UPDATES APPLICATION STATUS
    // =========================================================

    @Override
    @Transactional
    public Application updateStatus(
            Long applicationId,
            Long recruiterId,
            String status) {

        Application application = getApplicationById(applicationId);

        // Check that the application belongs to this recruiter's job
        if (!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiterId)) {

            throw new UnauthorizedException(
                    "You cannot update someone else's application.");
        }

        // Only allow valid statuses
        if (!status.equals("Applied")
                && !status.equals("Shortlisted")
                && !status.equals("Rejected")) {

            throw new IllegalArgumentException(
                    "Invalid status. Use Applied, Shortlisted or Rejected.");
        }

        application.setStatus(status);

        return applicationRepository.save(application);
    }
}