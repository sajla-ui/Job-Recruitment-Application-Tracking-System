package com.jobportal.candidate.repository;

import com.jobportal.candidate.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByCandidateId(Long candidateId);

    Optional<Resume> findTopByCandidateIdOrderByIdDesc(Long candidateId);
}