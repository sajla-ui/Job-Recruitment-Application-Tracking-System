package com.jobportal.candidate.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CandidateSkillId implements Serializable {

    @Column(name = "candidate_id")
    private Long candidateId;

    @Column(name = "skill_id")
    private Long skillId;

    public CandidateSkillId() {}

    public CandidateSkillId(Long candidateId, Long skillId) {
        this.candidateId = candidateId;
        this.skillId = skillId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateSkillId that = (CandidateSkillId) o;
        return Objects.equals(candidateId, that.candidateId) &&
               Objects.equals(skillId, that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, skillId);
    }
}
