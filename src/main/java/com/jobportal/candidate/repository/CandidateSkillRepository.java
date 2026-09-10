package com.jobportal.candidate.repository;

import com.jobportal.candidate.model.CandidateSkill;
import com.jobportal.candidate.model.CandidateSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, CandidateSkillId> {
    List<CandidateSkill> findByCandidateId(Long candidateId);
}
