package com.jobportal.application.service.impl;

import com.jobportal.application.model.Application;
import com.jobportal.application.model.Interview;
import com.jobportal.application.repository.ApplicationRepository;
import com.jobportal.application.repository.InterviewRepository;
import com.jobportal.application.service.InterviewService;
import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.repository.CandidateRepository;
import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.repository.RecruiterRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final RecruiterRepository recruiterRepository;
    private final CandidateRepository candidateRepository;

    public InterviewServiceImpl(
            InterviewRepository interviewRepository,
            ApplicationRepository applicationRepository,
            RecruiterRepository recruiterRepository,
            CandidateRepository candidateRepository) {

        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
        this.recruiterRepository = recruiterRepository;
        this.candidateRepository = candidateRepository;
    }


    // =====================================================
    // SCHEDULE INTERVIEW
    // =====================================================

    @Override
    @Transactional
    public Interview scheduleInterview(
            Long applicationId,
            Long recruiterId,
            LocalDateTime interviewDate,
            String mode,
            String location,
            String meetingLink,
            String notes) {

        // Find application
        Application application =
                applicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Application not found."
                                )
                        );


        // Find recruiter
        Recruiter recruiter =
                recruiterRepository.findById(recruiterId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter not found."
                                )
                        );


        // Make sure this recruiter owns the job
        if (!application.getJob()
                .getRecruiter()
                .getId()
                .equals(recruiter.getId())) {

            throw new RuntimeException(
                    "You are not authorized to schedule an interview for this application."
            );
        }


        // Only shortlisted candidates can be interviewed
        if (!"Shortlisted".equals(application.getStatus())) {

            throw new RuntimeException(
                    "Only shortlisted candidates can have an interview scheduled."
            );
        }


        // Validate date
        if (interviewDate == null ||
                !interviewDate.isAfter(LocalDateTime.now())) {

            throw new RuntimeException(
                    "Interview date must be in the future."
            );
        }


        // Validate mode
        if (!"Online".equalsIgnoreCase(mode) &&
                !"Offline".equalsIgnoreCase(mode)) {

            throw new RuntimeException(
                    "Interview mode must be Online or Offline."
            );
        }


        // Prevent multiple interviews for the same application
        List<Interview> existingInterviews =
                interviewRepository.findByApplicationId(applicationId);

        if (!existingInterviews.isEmpty()) {

            throw new RuntimeException(
                    "An interview has already been scheduled for this application."
            );
        }


        // Create interview
        Interview interview =
                new Interview(
                        application,
                        interviewDate,
                        mode,
                        location,
                        meetingLink,
                        notes
                );


        return interviewRepository.save(interview);
    }


    // =====================================================
    // GET INTERVIEW BY ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Interview getInterviewById(Long interviewId) {

        return interviewRepository.findById(interviewId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Interview not found."
                        )
                );
    }


    // =====================================================
    // GET CANDIDATE INTERVIEWS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getCandidateInterviews(
            Long candidateId) {

        Candidate candidate =
                candidateRepository.findById(candidateId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate not found."
                                )
                        );

        return interviewRepository
                .findByApplicationCandidate(candidate);
    }


    // =====================================================
    // GET RECRUITER INTERVIEWS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<Interview> getRecruiterInterviews(
            Long recruiterId) {

        Recruiter recruiter =
                recruiterRepository.findById(recruiterId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter not found."
                                )
                        );

        return interviewRepository
                .findByApplicationJobRecruiter(recruiter);
    }


    // =====================================================
    // UPDATE INTERVIEW STATUS
    // =====================================================

@Override
@Transactional
public Interview updateInterviewStatus(
        Long interviewId,
        Long recruiterId,
        String status) {

    Interview interview =
            getInterviewById(interviewId);

    // Check recruiter
    Long interviewRecruiterId =
            interview
                    .getApplication()
                    .getJob()
                    .getRecruiter()
                    .getId();

    if (!interviewRecruiterId.equals(recruiterId)) {

        throw new RuntimeException(
                "You are not authorized to update this interview."
        );
    }

    // Validate status
    if (!"Scheduled".equals(status) &&
            !"Completed".equals(status) &&
            !"Cancelled".equals(status)) {

        throw new RuntimeException(
                "Invalid interview status."
        );
    }

    // Update interview status
    
    
interview.setStatus(status);

// When the interview is completed,
// update the application status as well.
if ("Completed".equals(status)) {
    Application application = interview.getApplication();

    application.setStatus("Interview Completed");

    applicationRepository.save(application);
}

return interviewRepository.save(interview);
}


    // =====================================================
    // CANCEL INTERVIEW
    // =====================================================

    @Override
    @Transactional
    public void cancelInterview(
            Long interviewId,
            Long recruiterId) {

        Interview interview =
                getInterviewById(interviewId);


        Long interviewRecruiterId =
                interview
                        .getApplication()
                        .getJob()
                        .getRecruiter()
                        .getId();


        if (!interviewRecruiterId.equals(recruiterId)) {

            throw new RuntimeException(
                    "You are not authorized to cancel this interview."
            );
        }


        interview.setStatus("Cancelled");

        interviewRepository.save(interview);
    }
    @Override
@Transactional(readOnly = true)
public boolean hasInterview(Long applicationId) {
    return interviewRepository.existsByApplicationId(applicationId);
}
}