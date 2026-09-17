package com.jobportal.application.dto;

import java.time.LocalDateTime;

public class InterviewResponse {

    private Long id;
    private Long applicationId;

    private String jobTitle;
    private String companyName;

    private LocalDateTime interviewDate;
    private String mode;
    private String location;
    private String meetingLink;
    private String notes;
    private String status;

    private LocalDateTime appliedDate;

    public InterviewResponse() {
    }

    public InterviewResponse(
            Long id,
            Long applicationId,
            String jobTitle,
            String companyName,
            LocalDateTime interviewDate,
            String mode,
            String location,
            String meetingLink,
            String notes,
            String status,
            LocalDateTime appliedDate) {

        this.id = id;
        this.applicationId = applicationId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.interviewDate = interviewDate;
        this.mode = mode;
        this.location = location;
        this.meetingLink = meetingLink;
        this.notes = notes;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDateTime appliedDate) {
        this.appliedDate = appliedDate;
    }
}