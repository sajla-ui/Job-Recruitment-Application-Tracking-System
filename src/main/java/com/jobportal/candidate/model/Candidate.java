package com.jobportal.candidate.model;

import com.jobportal.shared.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrating OOP Concept: Inheritance (Subclass of User)
 * Demonstrating OOP Concept: Composition (Candidate has Education, Experience, Skills, Resumes)
 */
@Entity
@Table(name = "candidates")
@PrimaryKeyJoinColumn(name = "user_id") // Links to users table's id
public class Candidate extends User {

    @Column(nullable = false)
    private String name;

    private String phone;

    private String location;

    @Column(name = "profile_summary", columnDefinition = "TEXT")
    private String profileSummary;

    // Composition: Candidate 'has-a' list of Education records
    @JsonIgnore
@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Education> educations = new ArrayList<>();

    // Composition: Candidate 'has-a' list of Experience records
    
    @JsonIgnore
@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Experience> experiences = new ArrayList<>();

    // Composition: Candidate 'has-a' list of Resume
    @JsonIgnore
@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Resume> resumes = new ArrayList<>();

    // Composition: Candidate 'has-a' list of Skills (with level)
    
    @JsonIgnore
@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
private List<CandidateSkill> candidateSkills = new ArrayList<>();

    // Demonstrating OOP Constructors
    public Candidate() {
        super();
        this.setRole("CANDIDATE");
    }

    public Candidate(String email, String password, String name) {
        super(email, password, "CANDIDATE");
        this.name = name;
    }

    // Encapsulation: Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileSummary() {
        return profileSummary;
    }

    public void setProfileSummary(String profileSummary) {
        this.profileSummary = profileSummary;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public void addEducation(Education education) {
        educations.add(education);
        education.setCandidate(this);
    }

    public void removeEducation(Education education) {
        educations.remove(education);
        education.setCandidate(null);
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public void addExperience(Experience experience) {
        experiences.add(experience);
        experience.setCandidate(this);
    }

    public void removeExperience(Experience experience) {
        experiences.remove(experience);
        experience.setCandidate(null);
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }

    public void addResume(Resume resume) {
        resumes.add(resume);
        resume.setCandidate(this);
    }

    public void removeResume(Resume resume) {
        resumes.remove(resume);
        resume.setCandidate(null);
    }

    public List<CandidateSkill> getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(List<CandidateSkill> candidateSkills) {
        this.candidateSkills = candidateSkills;
    }

    public void addCandidateSkill(CandidateSkill candidateSkill) {
        candidateSkills.add(candidateSkill);
        candidateSkill.setCandidate(this);
    }

    public void removeCandidateSkill(CandidateSkill candidateSkill) {
        candidateSkills.remove(candidateSkill);
        candidateSkill.setCandidate(null);
    }
}
