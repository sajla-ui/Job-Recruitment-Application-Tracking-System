package com.jobportal.application.repository;

import com.jobportal.application.model.Interview;
import com.jobportal.candidate.model.Candidate;
import com.jobportal.recruiter.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    // Get interviews for a particular candidate
    List<Interview> findByApplicationCandidate(Candidate candidate);

    // Get interviews for a recruiter's applications
    List<Interview> findByApplicationJobRecruiter(Recruiter recruiter);

    // Get interview for a particular application
    List<Interview> findByApplicationId(Long applicationId);
    boolean existsByApplicationId(Long applicationId);
}