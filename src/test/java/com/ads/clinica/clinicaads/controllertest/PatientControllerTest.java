package com.ads.clinica.clinicaads.controllertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ads.clinica.clinicaads.controller.PatientController;
import com.ads.clinica.clinicaads.models.Patient;
import com.ads.clinica.clinicaads.repository.PatientRepository;

public class PatientControllerTest {
    
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPatientsByAge_ReturnsPatientsList_WhenPatientsFound() {
        // Arrange
        int age = 30;
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setFirstName("John");
        patient1.setLastName("Doe");
        patient1.setAge(age);

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setFirstName("Jane");
        patient2.setLastName("Smith");
        patient2.setAge(age);

        List<Patient> patients = Arrays.asList(patient1, patient2);
        when(patientRepository.findByAge(age)).thenReturn(patients);

        // Act
        ResponseEntity<List<Patient>> response = patientController.getPatientsByAge(age);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(patientRepository, times(1)).findByAge(age);
    }

    @Test
    void getPatientsByAge_ReturnsNoContent_WhenNoPatientsFound() {
        // Arrange
        int age = 40;
        when(patientRepository.findByAge(age)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Patient>> response = patientController.getPatientsByAge(age);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientRepository, times(1)).findByAge(age);
    }

    @Test
    void getPatientsByAge_ReturnsInternalServerError_WhenExceptionThrown() {
        // Arrange
        int age = 50;
        when(patientRepository.findByAge(age)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<Patient>> response = patientController.getPatientsByAge(age);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(patientRepository, times(1)).findByAge(age);
    }
    
}
