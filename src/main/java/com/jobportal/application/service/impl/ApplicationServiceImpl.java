package com.jobportal.application.service.impl;

import com.jobportal.application.model.Application;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.service.ApplicationService;
import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.model.Resume;
import com.jobportal.candidate.repository.CandidateRepository;
import com.jobportal.candidate.repository.ResumeRepository;
import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.repository.JobRepository;
import com.jobportal.recruiter.repository.RecruiterRepository;
import com.jobportal.shared.exception.DuplicateResourceException;
import com.jobportal.shared.exception.ResourceNotFoundException;
import com.jobportal.shared.exception.UnauthorizedException;
import com.jobportal.application.model.Interview;
import com.jobportal.application.repository.InterviewRepository;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CandidateRepository candidateRepository;
private final ResumeRepository resumeRepository;
private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
private final InterviewRepository interviewRepository;
    public ApplicationServiceImpl(
        ApplicationRepository applicationRepository,
        CandidateRepository candidateRepository,
        ResumeRepository resumeRepository,
        JobRepository jobRepository,
        RecruiterRepository recruiterRepository,
        InterviewRepository interviewRepository) {

    this.applicationRepository = applicationRepository;
    this.candidateRepository = candidateRepository;
    this.resumeRepository = resumeRepository;
    this.jobRepository = jobRepository;
    this.recruiterRepository = recruiterRepository;
    this.interviewRepository = interviewRepository;
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
        // Only allow valid statuses
if (!status.equals("Applied")
        && !status.equals("Shortlisted")
        && !status.equals("Rejected")
        && !status.equals("Interview Completed")
        && !status.equals("Selected")
        && !status.equals("Not Selected")) {

    throw new IllegalArgumentException(
            "Invalid application status.");
}

// Selected / Not Selected is allowed only after interview completion
if ("Selected".equals(status) || "Not Selected".equals(status)) {

    List<Interview> interviews =
            interviewRepository.findByApplicationId(applicationId);

    boolean interviewCompleted = interviews.stream()
            .anyMatch(interview ->
                    "Completed".equals(interview.getStatus()));

    if (!interviewCompleted) {
        throw new IllegalArgumentException(
                "The interview must be completed before final selection.");
    }
}

        application.setStatus(status);

        return applicationRepository.save(application);
    }
    @Override
@Transactional
public Application updateResult(
        Long applicationId,
        Long recruiterId,
        String status,
        String resultRemarks) {

    Application application = getApplicationById(applicationId);

    // Check whether this recruiter owns the job
    if (!application.getJob().getRecruiter().getId().equals(recruiterId)) {
        throw new UnauthorizedException(
                "You cannot update someone else's application.");
    }

    // Only final results are allowed here
    if (!"Selected".equals(status) && !"Not Selected".equals(status)) {
        throw new IllegalArgumentException(
                "Invalid final result status.");
    }

    // Final result requires a completed interview
    List<Interview> interviews =
            interviewRepository.findByApplicationId(applicationId);

    boolean interviewCompleted = interviews.stream()
            .anyMatch(interview ->
                    "Completed".equals(interview.getStatus()));

    if (!interviewCompleted) {
        throw new IllegalArgumentException(
                "The interview must be completed before giving the final result.");
    }

    application.setStatus(status);
    application.setResultRemarks(resultRemarks);

    return applicationRepository.save(application);
}
    // =========================================================
// RECRUITER VIEWS APPLICANT RESUME
// =========================================================

@Override
@Transactional(readOnly = true)
public Resume getCandidateResumeForRecruiter(
        Long applicationId,
        Long recruiterId) {

    Application application = getApplicationById(applicationId);

    // Make sure this application belongs to this recruiter's job
    if (!application.getJob()
            .getRecruiter()
            .getId()
            .equals(recruiterId)) {

        throw new UnauthorizedException(
                "You cannot view the resume for someone else's application.");
    }

    // Get the latest uploaded resume of the candidate
    return resumeRepository
            .findTopByCandidateIdOrderByIdDesc(
                    application.getCandidate().getId())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "No resume found for this candidate."));
}
}