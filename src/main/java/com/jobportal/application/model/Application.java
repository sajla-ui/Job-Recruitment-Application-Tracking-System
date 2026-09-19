package com.jobportal.application.model;

import com.jobportal.candidate.model.Candidate;
import com.jobportal.recruiter.model.Job;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "applications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"candidate_id", "job_id"})
    }
)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Candidate who applied
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    // Job applied for
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    // Applied / Shortlisted / Rejected
    @Column(nullable = false)
    private String status = "Applied";

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;
    @Column(name = "result_remarks", columnDefinition = "TEXT")
private String resultRemarks;

    // Default constructor
    public Application() {
        this.status = "Applied";
        this.appliedDate = LocalDateTime.now();
    }

    // Parameterized constructor
    public Application(Candidate candidate, Job job, String coverLetter) {
        this.candidate = candidate;
        this.job = job;
        this.coverLetter = coverLetter;
        this.status = "Applied";
        this.appliedDate = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
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

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    public String getResultRemarks() {
    return resultRemarks;
}

public void setResultRemarks(String resultRemarks) {
    this.resultRemarks = resultRemarks;
}
}