package com.jobportal;

import com.jobportal.candidate.model.Candidate;
import com.jobportal.candidate.repository.CandidateRepository;
import com.jobportal.candidate.service.impl.CandidateServiceImpl;
import com.jobportal.shared.exception.DuplicateResourceException;
import com.jobportal.shared.repository.UserRepository;
import com.jobportal.shared.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateServiceImpl candidateService;

    private Candidate testCandidate;

    @BeforeEach
    void setUp() {
        testCandidate = new Candidate();
        testCandidate.setEmail("test@example.com");
        testCandidate.setPassword("password123");
        testCandidate.setName("Test User");
    }

    @Test
    void testRegisterCandidate_Success() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(i -> i.getArguments()[0]);

        Candidate registered = candidateService.registerCandidate(testCandidate);

        assertNotNull(registered);
        assertEquals("CANDIDATE", registered.getRole());
        assertTrue(PasswordUtil.checkPassword("password123", registered.getPassword()));
        verify(candidateRepository, times(1)).save(any(Candidate.class));
    }

    @Test
    void testRegisterCandidate_DuplicateEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            candidateService.registerCandidate(testCandidate);
        });

        verify(candidateRepository, never()).save(any(Candidate.class));
    }
}
