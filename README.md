# Job Recruitment and Application Tracking System

## Candidate Module (Member 1)

This repository contains the Candidate Module for the Job Recruitment and Application Tracking System. 
This module focuses exclusively on the candidate's journey: registration, profile management (education, experience, skills, resume), and viewing available jobs.

### Features
- **Candidate Registration & Login**: Secure password hashing (BCrypt) with session-based authentication.
- **Profile Management**: Update basic details, phone, location, and summary.
- **Skills Management**: Add and manage professional skills and proficiency levels.
- **Education & Experience History**: Full CRUD operations for career timeline.
- **Resume Management**: Secure resume file uploads (stored safely outside Git).
- **Job Browsing**: View available jobs (Integration point with Member 2's module).

### Technologies Used
- Java 17
- Spring Boot 3 (Web, Data JPA, Validation)
- MySQL
- HTML5, CSS3, JavaScript (Vanilla fetch API)
- Maven

### Object-Oriented Programming (OOP) Demonstrations
This project heavily emphasizes OOP principles for academic evaluation:
1. **Inheritance**: Demonstrated by the `Candidate` class extending the `User` base class. Uses JPA `@Inheritance(strategy = InheritanceType.JOINED)`.
2. **Encapsulation**: All entity attributes are `private` with public `getters` and `setters`. Data Transfer Objects (DTOs) protect the internal model layer from the API layer.
3. **Abstraction**: The `CandidateService` and `JobIntegrationService` interfaces hide the implementation details from the controllers.
4. **Polymorphism**: Demonstrated through interface implementation (`CandidateServiceImpl`).
5. **Composition**: The `Candidate` class is composed of collections of `Education`, `Experience`, `Resume`, and `CandidateSkill` objects.
6. **Constructors**: Parameterized constructors are used extensively for dependency injection in services and object creation.

### Database Setup
1. Install MySQL.
2. Create a database: `CREATE DATABASE jobportal_db;`
3. Configure `src/main/resources/application.properties` (use `application.properties.example` as a guide).
4. Run the application; Hibernate will automatically execute `schema.sql` and create the required tables.

### How to Run
1. Navigate to the project root.
2. Build the project: `./mvnw clean install` (or `mvn clean install`)
3. Run the application: `./mvnw spring-boot:run` (or `mvn spring-boot:run`)
4. Access the application in your browser at: `http://localhost:8080/`

### Team Module Division
- **Member 1**: Candidate Module (This repository code)
- **Member 2**: Company/Recruiter Module (Job postings, recruiter profiles)
- **Member 3**: Application & Interview Module (Connecting Candidates to Jobs)
- **Member 4**: Database & Reports Module (System-wide reporting)

Please refer to `INTEGRATION.md` for details on how other members should connect with this code.
