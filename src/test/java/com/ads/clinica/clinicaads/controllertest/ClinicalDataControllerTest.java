package com.ads.clinica.clinicaads.controllertest;

import com.ads.clinica.clinicaads.controller.ClinicalDataController;
import com.ads.clinica.clinicaads.dto.ClinicalDataDTO;
import com.ads.clinica.clinicaads.models.ClinicalData;
import com.ads.clinica.clinicaads.models.Patient;
import com.ads.clinica.clinicaads.repository.ClinicalDataRepository;
import com.ads.clinica.clinicaads.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClinicalDataControllerTest {

    @Mock
    private ClinicalDataRepository clinicalDataRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private ClinicalDataController clinicalDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // CREATE
    @Test
    void createClinicalData_ReturnsCreated_WhenSaved() {
        ClinicalData clinicalData = new ClinicalData();
        when(clinicalDataRepository.save(clinicalData)).thenReturn(clinicalData);

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalData(clinicalData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
        verify(clinicalDataRepository, times(1)).save(clinicalData);
    }

    @Test
    void createClinicalData_ReturnsInternalServerError_OnException() {
        ClinicalData clinicalData = new ClinicalData();
        when(clinicalDataRepository.save(clinicalData)).thenThrow(new RuntimeException());

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalData(clinicalData);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createClinicalDataForPatient_ReturnsCreated_WhenPatientExists() {
        ClinicalData clinicalData = new ClinicalData();
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(clinicalDataRepository.save(clinicalData)).thenReturn(clinicalData);

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalDataForPatient(1L, clinicalData);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
        assertEquals(patient, clinicalData.getPatient());
        verify(clinicalDataRepository, times(1)).save(clinicalData);
    }

    @Test
    void createClinicalDataForPatient_ReturnsNotFound_WhenPatientDoesNotExist() {
        ClinicalData clinicalData = new ClinicalData();
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalDataForPatient(1L, clinicalData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createClinicalDataForPatient_ReturnsInternalServerError_OnException() {
        ClinicalData clinicalData = new ClinicalData();
        when(patientRepository.findById(1L)).thenThrow(new RuntimeException());

        ResponseEntity<ClinicalData> response = clinicalDataController.createClinicalDataForPatient(1L, clinicalData);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // READ
    @Test
    void getAllClinicalData_ReturnsList_WhenFound() {
        ClinicalData cd1 = new ClinicalData();
        ClinicalData cd2 = new ClinicalData();
        List<ClinicalData> list = Arrays.asList(cd1, cd2);
        when(clinicalDataRepository.findAll()).thenReturn(list);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getAllClinicalData();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getAllClinicalData_ReturnsNoContent_WhenEmpty() {
        when(clinicalDataRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getAllClinicalData();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllClinicalData_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getAllClinicalData();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataById_ReturnsClinicalData_WhenFound() {
        ClinicalData clinicalData = new ClinicalData();
        when(clinicalDataRepository.findById(1L)).thenReturn(Optional.of(clinicalData));

        ResponseEntity<ClinicalData> response = clinicalDataController.getClinicalDataById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
    }

    @Test
    void getClinicalDataById_ReturnsNotFound_WhenNotFound() {
        when(clinicalDataRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ClinicalData> response = clinicalDataController.getClinicalDataById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByPatientId_ReturnsList_WhenFound() {
        ClinicalData cd = new ClinicalData();
        List<ClinicalData> list = Collections.singletonList(cd);
        when(clinicalDataRepository.findByPatientId(1L)).thenReturn(list);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getClinicalDataByPatientId_ReturnsNoContent_WhenEmpty() {
        when(clinicalDataRepository.findByPatientId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByPatientId_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.findByPatientId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByComponentName_ReturnsList_WhenFound() {
        ClinicalData cd = new ClinicalData();
        List<ClinicalData> list = Collections.singletonList(cd);
        when(clinicalDataRepository.findByComponentName("glucose")).thenReturn(list);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByComponentName("glucose");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getClinicalDataByComponentName_ReturnsNoContent_WhenEmpty() {
        when(clinicalDataRepository.findByComponentName("glucose")).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByComponentName("glucose");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByComponentName_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.findByComponentName("glucose")).thenThrow(new RuntimeException());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByComponentName("glucose");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getLatestClinicalDataByPatientId_ReturnsList_WhenFound() {
        ClinicalData cd = new ClinicalData();
        List<ClinicalData> list = Collections.singletonList(cd);
        when(clinicalDataRepository.findLatestClinicalDataByPatientId(1L)).thenReturn(list);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getLatestClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getLatestClinicalDataByPatientId_ReturnsNoContent_WhenEmpty() {
        when(clinicalDataRepository.findLatestClinicalDataByPatientId(1L)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getLatestClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getLatestClinicalDataByPatientId_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.findLatestClinicalDataByPatientId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getLatestClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByPatientAndComponent_ReturnsList_WhenFound() {
        ClinicalData cd = new ClinicalData();
        List<ClinicalData> list = Collections.singletonList(cd);
        when(clinicalDataRepository.findByPatientIdAndComponentName(1L, "glucose")).thenReturn(list);

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientAndComponent(1L,
                "glucose");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
    }

    @Test
    void getClinicalDataByPatientAndComponent_ReturnsNoContent_WhenEmpty() {
        when(clinicalDataRepository.findByPatientIdAndComponentName(1L, "glucose")).thenReturn(Collections.emptyList());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientAndComponent(1L,
                "glucose");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getClinicalDataByPatientAndComponent_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.findByPatientIdAndComponentName(1L, "glucose")).thenThrow(new RuntimeException());

        ResponseEntity<List<ClinicalData>> response = clinicalDataController.getClinicalDataByPatientAndComponent(1L,
                "glucose");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // UPDATE
    @Test
    void updateClinicalData_ReturnsUpdated_WhenFound() {
        ClinicalData existing = new ClinicalData();
        ClinicalData update = new ClinicalData();
        update.setComponentName("glucose");
        update.setComponentValue("100");
        update.setMeasuredDateTime(new Timestamp(System.currentTimeMillis()));

        when(clinicalDataRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clinicalDataRepository.save(existing)).thenReturn(existing);

        ResponseEntity<ClinicalData> response = clinicalDataController.updateClinicalData(1L, update);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existing, response.getBody());
        assertEquals("glucose", existing.getComponentName());
        assertEquals("100", existing.getComponentValue());
    }

    @Test
    void updateClinicalData_ReturnsNotFound_WhenNotFound() {
        ClinicalData update = new ClinicalData();
        when(clinicalDataRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ClinicalData> response = clinicalDataController.updateClinicalData(1L, update);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // DELETE
    @Test
    void deleteClinicalData_ReturnsNoContent_WhenExists() {
        when(clinicalDataRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalData(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clinicalDataRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteClinicalData_ReturnsNotFound_WhenNotExists() {
        when(clinicalDataRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalData(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteClinicalData_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.existsById(1L)).thenThrow(new RuntimeException());

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalData(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteClinicalDataByPatientId_ReturnsNoContent_WhenDeleted() {
        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clinicalDataRepository, times(1)).deleteByPatientId(1L);
    }

    @Test
    void deleteClinicalDataByPatientId_ReturnsInternalServerError_OnException() {
        doThrow(new RuntimeException()).when(clinicalDataRepository).deleteByPatientId(1L);

        ResponseEntity<HttpStatus> response = clinicalDataController.deleteClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // UTILITY
    @Test
    void countClinicalDataByPatientId_ReturnsCount_WhenFound() {
        when(clinicalDataRepository.countByPatientId(1L)).thenReturn(5L);

        ResponseEntity<Long> response = clinicalDataController.countClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void countClinicalDataByPatientId_ReturnsInternalServerError_OnException() {
        when(clinicalDataRepository.countByPatientId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<Long> response = clinicalDataController.countClinicalDataByPatientId(1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // DTO save
    @Test
    void saveClinicalData_ReturnsCreated_WhenPatientExists() {
        ClinicalDataDTO dto = new ClinicalDataDTO();
        dto.setComponentName("glucose");
        dto.setComponentValue("100");
        dto.setPatientId(1L);

        Patient patient = new Patient();
        patient.setId(1L);

        ClinicalData clinicalData = new ClinicalData();
        clinicalData.setComponentName(dto.getComponentName());
        clinicalData.setComponentValue(dto.getComponentValue());
        clinicalData.setPatient(patient);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(clinicalDataRepository.save(any(ClinicalData.class))).thenReturn(clinicalData);

        ResponseEntity<ClinicalData> response = clinicalDataController.saveClinicalData(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clinicalData, response.getBody());
    }

    @Test
    void saveClinicalData_ReturnsInternalServerError_OnException() {
        ClinicalDataDTO dto = new ClinicalDataDTO();
        dto.setComponentName("glucose");
        dto.setComponentValue("100");
        dto.setPatientId(1L);

        when(patientRepository.findById(1L)).thenThrow(new RuntimeException());

        ResponseEntity<ClinicalData> response = clinicalDataController.saveClinicalData(dto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}