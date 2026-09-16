package com.jobportal.recruiter.service.impl;

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

    // Constructor - Dependency Injection
    public JobServiceImpl(
            JobRepository jobRepository,
            RecruiterRepository recruiterRepository) {

        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
    }

    // Create / Post Job
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

    // Get one job
    @Override
    public Job getJobById(Long jobId) {

        return jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Job not found with id " + jobId
                        )
                );
    }

    // Get all jobs
    @Override
    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    // Get jobs belonging to a recruiter
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

    // Update Job
    @Override
    @Transactional
    public Job updateJob(
            Long recruiterId,
            Long jobId,
            Job updatedJob) {

        Job existingJob = getJobById(jobId);

        // Security check:
        // Only the recruiter who posted the job can update it.
        if (!existingJob.getRecruiter()
                .getId()
                .equals(recruiterId)) {

            throw new UnauthorizedException(
                    "You cannot update someone else's job."
            );
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setLocation(updatedJob.getLocation());
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

    // Delete Job
    @Override
    @Transactional
    public void deleteJob(
            Long recruiterId,
            Long jobId) {

        Job existingJob = getJobById(jobId);

        // Security check
        if (!existingJob.getRecruiter()
                .getId()
                .equals(recruiterId)) {

            throw new UnauthorizedException(
                    "You cannot delete someone else's job."
            );
        }

        jobRepository.delete(existingJob);
    }

    // Search Jobs
    @Override
    public List<Job> searchJobs(
            String keyword,
            String location) {

        boolean hasKeyword =
                keyword != null && !keyword.trim().isEmpty();

        boolean hasLocation =
                location != null && !location.trim().isEmpty();

        if (hasKeyword && hasLocation) {

            return jobRepository
                    .findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(
                            keyword,
                            location
                    );

        } else if (hasKeyword) {

            return jobRepository
                    .findByTitleContainingIgnoreCase(keyword);

        } else if (hasLocation) {

            return jobRepository
                    .findByLocationContainingIgnoreCase(location);

        } else {

            return jobRepository.findAll();
        }
    }
}