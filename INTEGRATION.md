# Integration Guide

This document outlines how Member 1's **Candidate Module** interacts with the rest of the system.
Please follow these guidelines to ensure smooth integration of all 4 modules into one GitHub repository.

## Member 1 Ownership (Do NOT modify these without discussion)
**Database Tables:**
- `users` (Base table, extend from this)
- `candidates`
- `skills`
- `candidate_skills`
- `education`
- `experience`
- `resumes`

**Packages:**
- `com.jobportal.candidate.*`
- `com.jobportal.shared.*`

---

## Guide for Member 2 (Company/Recruiter Module)

### 1. User Inheritance
When creating the `Recruiter` or `CompanyUser` entity, you **must** extend the shared `User` class to maintain consistency in authentication.
```java
import com.jobportal.shared.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "recruiters")
@PrimaryKeyJoinColumn(name = "user_id")
public class Recruiter extends User {
    // recruiter specific fields (company name, etc)
}
```

### 2. Jobs Implementation
Member 1 has created a read-only interface `JobIntegrationService` and a `JobDTO`. 
Member 1 currently uses a **mock implementation** (`MockJobIntegrationServiceImpl`) so the Candidate UI can function.

**Your Task:**
1. Create the `jobs` database table and `Job` entity.
2. Implement your own services.
3. Replace `MockJobIntegrationServiceImpl` with a real implementation that queries your `Job` entity, maps it to `JobDTO`, and returns it.

---

## Guide for Member 3 (Application & Interview Module)

You will bridge the gap between Member 1 (`Candidate`) and Member 2 (`Job`).

### 1. Application Entity
Your `Application` entity will likely map `candidate_id` and `job_id`.
```java
@Entity
public class JobApplication {
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    // Use Member 2's Job Entity
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
    
    private String status; // APPLIED, INTERVIEWING, REJECTED, HIRED
}
```

### 2. UI Integration
On the Candidate's "Browse Jobs" page (built by Member 1), there is a placeholder for the "Apply" button. 
You will need to write the JavaScript/API logic to handle when a Candidate clicks "Apply".

---

## Guide for Member 4 (Database & Reports Module)

You have full read access to Member 1's tables.
If you need complex queries (e.g., "Find all candidates with 'Java' skill and >3 years experience"), write custom `@Query` methods in new Repositories or use `JdbcTemplate` in your reporting services. 

Do not alter the structure of the Candidate entities unless absolutely necessary.
