package com.jobportal.candidate.service.impl;

import com.jobportal.candidate.dto.JobDTO;
import com.jobportal.candidate.service.JobIntegrationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Temporary mock implementation of JobIntegrationService.
 * This allows Member 1 (Candidate Module) to build and test their UI 
 * without waiting for Member 2 (Company Module) to finish the database and logic.
 * 
 * ONCE MEMBER 2 IS READY: Replace this with a real implementation that queries 
 * the actual 'jobs' table.
 */
@Service
public class MockJobIntegrationServiceImpl implements JobIntegrationService {

    private final List<JobDTO> mockJobs = new ArrayList<>();

    public MockJobIntegrationServiceImpl() {
        mockJobs.add(new JobDTO(1L, "Software Engineer", "TechCorp", "New York, NY", "Full-time", 
                "Develop and maintain web applications.", "Java, Spring Boot, MySQL", LocalDateTime.now().minusDays(2)));
        mockJobs.add(new JobDTO(2L, "Frontend Developer", "WebSolutions", "Remote", "Contract", 
                "Build responsive UI using React.", "HTML, CSS, JavaScript, React", LocalDateTime.now().minusDays(5)));
        mockJobs.add(new JobDTO(3L, "Data Analyst", "DataInsights", "San Francisco, CA", "Full-time", 
                "Analyze large datasets to find trends.", "Python, SQL, Tableau", LocalDateTime.now().minusDays(10)));
    }

    @Override
    public List<JobDTO> getAllAvailableJobs() {
        return mockJobs;
    }

    @Override
    public JobDTO getJobById(Long jobId) {
        return mockJobs.stream()
                .filter(job -> job.getId().equals(jobId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<JobDTO> searchJobs(String keyword, String location) {
        return mockJobs.stream()
                .filter(job -> (keyword == null || job.getTitle().toLowerCase().contains(keyword.toLowerCase())) &&
                               (location == null || job.getLocation().toLowerCase().contains(location.toLowerCase())))
                .collect(Collectors.toList());
    }
}
