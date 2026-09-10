package com.jobportal.candidate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "candidate_skills")
public class CandidateSkill {

    @EmbeddedId
    private CandidateSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("candidateId")
    @JoinColumn(name = "candidate_id")
    @JsonIgnore
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(name = "skill_level")
    private String skillLevel;

    public CandidateSkill() {}

    public CandidateSkill(Candidate candidate, Skill skill, String skillLevel) {
        this.candidate = candidate;
        this.skill = skill;
        this.skillLevel = skillLevel;
        this.id = new CandidateSkillId(candidate.getId(), skill.getId());
    }

    public CandidateSkillId getId() {
        return id;
    }

    public void setId(CandidateSkillId id) {
        this.id = id;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}
