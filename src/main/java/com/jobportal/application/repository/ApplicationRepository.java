package com.jobportal.application.repository;

import com.jobportal.application.model.Application;
import com.jobportal.candidate.model.Candidate;
import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Get all applications for a particular candidate
    List<Application> findByCandidate(Candidate candidate);

    // Get all applications for a particular job
    List<Application> findByJob(Job job);

    // Get all applications received by a recruiter
    List<Application> findByJobRecruiter(Recruiter recruiter);

    // Check whether a candidate has already applied for a job
    boolean existsByCandidateAndJob(Candidate candidate, Job job);
}