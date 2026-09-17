package com.jobportal.application.controller;


import com.jobportal.application.dto.InterviewResponse;
import com.jobportal.application.model.Interview;
import com.jobportal.application.service.InterviewService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }


    // =====================================================
    // SCHEDULE INTERVIEW
    // =====================================================

    @PostMapping
    public ResponseEntity<Interview> scheduleInterview(
            @RequestParam Long applicationId,
            @RequestParam Long recruiterId,
            @RequestParam String interviewDate,
            @RequestParam String mode,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String meetingLink,
            @RequestParam(required = false) String notes) {

        LocalDateTime dateTime =
                LocalDateTime.parse(interviewDate);

        Interview interview =
                interviewService.scheduleInterview(
                        applicationId,
                        recruiterId,
                        dateTime,
                        mode,
                        location,
                        meetingLink,
                        notes
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(interview);
    }


    // =====================================================
    // GET INTERVIEW BY ID
    // =====================================================


    @GetMapping("/{id}")
public ResponseEntity<Interview> getInterview(@PathVariable Long id) {
    return ResponseEntity.ok(interviewService.getInterviewById(id));
}


@GetMapping("/application/{applicationId}/exists")
public ResponseEntity<Boolean> hasInterview(
        @PathVariable Long applicationId) {

    return ResponseEntity.ok(
        interviewService.hasInterview(applicationId)
    );
}


    // =====================================================
    // GET CANDIDATE INTERVIEWS
    // =====================================================

    @GetMapping("/candidate/{candidateId}")
public ResponseEntity<List<InterviewResponse>> getCandidateInterviews(
        @PathVariable Long candidateId) {

    List<Interview> interviews =
            interviewService.getCandidateInterviews(candidateId);

    List<InterviewResponse> response =
            interviews.stream()
                    .map(interview -> {

                        var application =
                                interview.getApplication();

                        var job =
                                application.getJob();

                        return new InterviewResponse(
                                interview.getId(),
                                application.getId(),
                                job.getTitle(),
                                job.getCompany(),
                                interview.getInterviewDate(),
                                interview.getMode(),
                                interview.getLocation(),
                                interview.getMeetingLink(),
                                interview.getNotes(),
                                interview.getStatus(),
                                application.getAppliedDate()
                        );
                    })
                    .toList();

    return ResponseEntity.ok(response);
}


    // =====================================================
    // GET RECRUITER INTERVIEWS
    // =====================================================

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Interview>> getRecruiterInterviews(
            @PathVariable Long recruiterId) {

        return ResponseEntity.ok(
                interviewService.getRecruiterInterviews(
                        recruiterId
                )
        );
    }


    // =====================================================
    // UPDATE INTERVIEW STATUS
    // =====================================================

    @PutMapping("/{id}/status")
    public ResponseEntity<Interview> updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam Long recruiterId,
            @RequestParam String status) {

        Interview interview =
                interviewService.updateInterviewStatus(
                        id,
                        recruiterId,
                        status
                );

        return ResponseEntity.ok(interview);
    }


    // =====================================================
    // CANCEL INTERVIEW
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelInterview(
            @PathVariable Long id,
            @RequestParam Long recruiterId) {

        interviewService.cancelInterview(
                id,
                recruiterId
        );

        return ResponseEntity.noContent().build();
    }
}