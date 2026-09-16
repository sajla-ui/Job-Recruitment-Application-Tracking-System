package com.jobportal.recruiter.repository;
import com.jobportal.recruiter.model.Job;
import com.jobportal.recruiter.model.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiter(Recruiter recruiter);

    List<Job> findByTitleContainingIgnoreCase(String keyword);

    List<Job> findByLocationContainingIgnoreCase(String location);

    List<Job> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String keyword,
            String location
    );
}