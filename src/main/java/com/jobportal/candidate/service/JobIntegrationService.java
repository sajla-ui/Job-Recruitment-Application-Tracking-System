package com.jobportal.candidate.service;

import com.jobportal.candidate.dto.JobDTO;
import java.util.List;

/**
 * INTEGRATION POINT FOR MEMBER 2.
 * 
 * This interface defines the contract that the Candidate module expects
 * to interact with Jobs. Member 2 (Company/Recruiter Module) should 
 * provide an implementation for this interface or expose internal APIs 
 * that fulfill these requirements.
 */
public interface JobIntegrationService {

    /**
     * Get a list of all available jobs.
     */
    List<JobDTO> getAllAvailableJobs();

    /**
     * Get details of a specific job.
     */
    JobDTO getJobById(Long jobId);

    /**
     * Search jobs by keyword or location.
     */
    List<JobDTO> searchJobs(String keyword, String location);
}
