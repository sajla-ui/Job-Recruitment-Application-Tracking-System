package com.jobportal.recruiter.service.impl;

import com.jobportal.application.model.Application;
import com.jobportal.application.model.Interview;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.repository.InterviewRepository;

import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.repository.JobRepository;
import com.jobportal.recruiter.repository.RecruiterRepository;
import com.jobportal.recruiter.service.JobService;

import com.jobportal.shared.exception.ResourceNotFoundException;
import com.jobportal.shared.exception.UnauthorizedException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;

    // =====================================================
    // CONSTRUCTOR - DEPENDENCY INJECTION
    // =====================================================

    public JobServiceImpl(
            JobRepository jobRepository,
            RecruiterRepository recruiterRepository,
            ApplicationRepository applicationRepository,
            InterviewRepository interviewRepository) {

        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
    }

    // =====================================================
    // CREATE / POST JOB
    // =====================================================

    @Override
    @Transactional
    public Job createJob(Long recruiterId, Job job) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recruiter not found with id " + recruiterId
                        )
                );

        // Connect job with recruiter
        job.setRecruiter(recruiter);

        // Use recruiter's company name
        job.setCompany(recruiter.getCompanyName());

        // Set posting date automatically
        job.setPostedDate(LocalDateTime.now());

        return jobRepository.save(job);
    }

    // =====================================================
    // GET ONE JOB
    // =====================================================

    @Override
    public Job getJobById(Long jobId) {

        return jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id " + jobId
                        )
                );
    }

    // =====================================================
    // GET ALL JOBS
    // =====================================================

    @Override
    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    // =====================================================
    // GET JOBS BELONGING TO A RECRUITER
    // =====================================================

    @Override
    public List<Job> getJobsByRecruiter(Long recruiterId) {

        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recruiter not found with id " + recruiterId
                        )
                );

        return jobRepository.findByRecruiter(recruiter);
    }

    // =====================================================
    // UPDATE JOB
    // =====================================================

    @Override
    @Transactional
    public Job updateJob(
            Long recruiterId,
            Long jobId,
            Job updatedJob) {

        Job existingJob = getJobById(jobId);

        // Security check:
        // Only the recruiter who posted the job
        // can update it.
        if (!existingJob.getRecruiter()
                .getId()
                .equals(recruiterId)) {

            throw new UnauthorizedException(
                    "You cannot update someone else's job."
            );
        }

        existingJob.setTitle(
                updatedJob.getTitle()
        );

        existingJob.setLocation(
                updatedJob.getLocation()
        );

        existingJob.setEmploymentType(
                updatedJob.getEmploymentType()
        );

        existingJob.setDescription(
                updatedJob.getDescription()
        );

        existingJob.setRequirements(
                updatedJob.getRequirements()
        );

        return jobRepository.save(existingJob);
    }

    // =====================================================
    // DELETE JOB
    // =====================================================

    @Override
    @Transactional
    public void deleteJob(
            Long recruiterId,
            Long jobId) {

        Job existingJob = getJobById(jobId);

        // -------------------------------------------------
        // SECURITY CHECK
        // -------------------------------------------------

        if (!existingJob.getRecruiter()
                .getId()
                .equals(recruiterId)) {

            throw new UnauthorizedException(
                    "You cannot delete someone else's job."
            );
        }

        // -------------------------------------------------
        // FIND ALL APPLICATIONS FOR THIS JOB
        // -------------------------------------------------

        List<Application> applications =
                applicationRepository.findByJob(existingJob);

        // -------------------------------------------------
        // DELETE INTERVIEWS FIRST
        // -------------------------------------------------
        // Interviews depend on applications.
        // Therefore, interviews must be deleted first.
        // -------------------------------------------------

        for (Application application : applications) {

            List<Interview> interviews =
                    interviewRepository.findByApplicationId(
                            application.getId()
                    );

            if (!interviews.isEmpty()) {

                interviewRepository.deleteAll(
                        interviews
                );
            }
        }

        // -------------------------------------------------
        // DELETE APPLICATIONS
        // -------------------------------------------------
        // Applications depend on the job.
        // Therefore, delete them before the job.
        // -------------------------------------------------

        if (!applications.isEmpty()) {

            applicationRepository.deleteAll(
                    applications
            );
        }

        // -------------------------------------------------
        // FINALLY DELETE THE JOB
        // -------------------------------------------------

        jobRepository.delete(existingJob);
    }

    // =====================================================
    // SEARCH JOBS
    // =====================================================

    @Override
    public List<Job> searchJobs(
            String keyword,
            String location) {

        boolean hasKeyword =
                keyword != null &&
                !keyword.trim().isEmpty();

        boolean hasLocation =
                location != null &&
                !location.trim().isEmpty();

        if (hasKeyword && hasLocation) {

            return jobRepository
                    .findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(
                            keyword,
                            location
                    );

        } else if (hasKeyword) {

            return jobRepository
                    .findByTitleContainingIgnoreCase(
                            keyword
                    );

        } else if (hasLocation) {

            return jobRepository
                    .findByLocationContainingIgnoreCase(
                            location
                    );

        } else {

            return jobRepository.findAll();
        }
    }
}