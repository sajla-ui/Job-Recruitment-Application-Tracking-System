package com.jobportal.recruiter.service;

import com.jobportal.recruiter.model.Recruiter;

public interface RecruiterService {

    // Registration
    Recruiter registerRecruiter(Recruiter recruiter);

    // Login
    Recruiter loginRecruiter(String email, String password);

    // Profile
    Recruiter getRecruiterById(Long id);

    Recruiter updateProfile(Long id, Recruiter updatedData);
}