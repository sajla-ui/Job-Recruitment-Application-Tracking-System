package com.jobportal.application.service;

import com.jobportal.application.model.Interview;

import java.time.LocalDateTime;
import java.util.List;

public interface InterviewService {

    // Schedule a new interview
    Interview scheduleInterview(
            Long applicationId,
            Long recruiterId,
            LocalDateTime interviewDate,
            String mode,
            String location,
            String meetingLink,
            String notes
    );

    // Get an interview by ID
    Interview getInterviewById(Long interviewId);

    // Get all interviews for a candidate
    List<Interview> getCandidateInterviews(Long candidateId);

    // Get all interviews for a recruiter
    List<Interview> getRecruiterInterviews(Long recruiterId);


    // Update interview status
    Interview updateInterviewStatus(
            Long interviewId,
            Long recruiterId,
            String status
    );

    // Delete/cancel an interview
    void cancelInterview(
            Long interviewId,
            Long recruiterId
    );
    boolean hasInterview(Long applicationId);
}