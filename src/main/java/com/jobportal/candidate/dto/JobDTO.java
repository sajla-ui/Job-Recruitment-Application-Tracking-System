package com.jobportal.candidate.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Job.
 * We do not own the Job Entity. This DTO represents the expected data
 * we need from Member 2's module to display jobs to the candidate.
 */
public class JobDTO {
    private Long id;
    private String title;
    private String company;
    private String location;
    private String employmentType;
    private String description;
    private String requirements;
    private LocalDateTime postedDate;

    public JobDTO() {}

    public JobDTO(Long id, String title, String company, String location, String employmentType, String description, String requirements, LocalDateTime postedDate) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.employmentType = employmentType;
        this.description = description;
        this.requirements = requirements;
        this.postedDate = postedDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }
}
