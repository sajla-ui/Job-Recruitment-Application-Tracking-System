package com.jobportal.recruiter.model;

import com.jobportal.shared.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "recruiters")
@PrimaryKeyJoinColumn(name = "user_id")
public class Recruiter extends User {

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Column(name = "website")
    private String website;

    @Column(name = "company_location")
    private String companyLocation;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "phone")
    private String phone;

    // Default constructor
    public Recruiter() {
        super();
        this.setRole("RECRUITER");
    }

    // Parameterized constructor
    public Recruiter(
            String email,
            String password,
            String companyName,
            String companyDescription,
            String website,
            String companyLocation,
            String contactPerson,
            String phone) {

        super(email, password, "RECRUITER");

        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.website = website;
        this.companyLocation = companyLocation;
        this.contactPerson = contactPerson;
        this.phone = phone;
    }

    // Getters and Setters

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}