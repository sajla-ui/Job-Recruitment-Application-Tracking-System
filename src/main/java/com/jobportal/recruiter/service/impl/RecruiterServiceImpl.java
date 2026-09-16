package com.jobportal.recruiter.service.impl;

import com.jobportal.recruiter.model.Recruiter;
import com.jobportal.recruiter.repository.RecruiterRepository;
import com.jobportal.recruiter.service.RecruiterService;
import com.jobportal.shared.exception.DuplicateResourceException;
import com.jobportal.shared.exception.ResourceNotFoundException;
import com.jobportal.shared.exception.UnauthorizedException;
import com.jobportal.shared.util.PasswordUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    private final RecruiterRepository recruiterRepository;

    // Constructor - Dependency Injection
    public RecruiterServiceImpl(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    // Register recruiter
    @Override
    @Transactional
    public Recruiter registerRecruiter(Recruiter recruiter) {

        // Check whether email already exists
        if (recruiterRepository.existsByEmail(recruiter.getEmail())) {
            throw new DuplicateResourceException(
                    "Email is already registered."
            );
        }

        // Hash password before saving
        recruiter.setPassword(
                PasswordUtil.hashPassword(recruiter.getPassword())
        );

        // Set role
        recruiter.setRole("RECRUITER");

        return recruiterRepository.save(recruiter);
    }

    // Login recruiter
    @Override
    public Recruiter loginRecruiter(
            String email,
            String password) {

        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Invalid email or password."
                        )
                );

        // Check password
        if (!PasswordUtil.checkPassword(
                password,
                recruiter.getPassword())) {

            throw new UnauthorizedException(
                    "Invalid email or password."
            );
        }

        return recruiter;
    }

    // Get recruiter profile
    @Override
    public Recruiter getRecruiterById(Long id) {

        return recruiterRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Recruiter not found with id " + id
                        )
                );
    }

    // Update recruiter profile
    @Override
    @Transactional
    public Recruiter updateProfile(
            Long id,
            Recruiter updatedData) {

        Recruiter existing = getRecruiterById(id);

        existing.setCompanyName(
                updatedData.getCompanyName()
        );

        existing.setCompanyDescription(
                updatedData.getCompanyDescription()
        );

        existing.setWebsite(
                updatedData.getWebsite()
        );

        existing.setCompanyLocation(
                updatedData.getCompanyLocation()
        );

        existing.setContactPerson(
                updatedData.getContactPerson()
        );

        existing.setPhone(
                updatedData.getPhone()
        );

        return recruiterRepository.save(existing);
    }
}