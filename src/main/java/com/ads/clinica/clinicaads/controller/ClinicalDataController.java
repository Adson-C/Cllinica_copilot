package com.ads.clinica.clinicaads.controller;

import com.ads.clinica.clinicaads.dto.ClinicalDataDTO;
import com.ads.clinica.clinicaads.models.ClinicalData;
import com.ads.clinica.clinicaads.models.Patient;
import com.ads.clinica.clinicaads.repository.ClinicalDataRepository;
import com.ads.clinica.clinicaads.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clinical-data")
@CrossOrigin(origins = "*")
public class ClinicalDataController {

    @Autowired
    private ClinicalDataRepository clinicalDataRepository;

    @Autowired
    private PatientRepository patientRepository;

    // CREATE - Criar um novo dado clínico
    @PostMapping
    public ResponseEntity<ClinicalData> createClinicalData(@RequestBody ClinicalData clinicalData) {
        try {
            ClinicalData savedClinicalData = clinicalDataRepository.save(clinicalData);
            return new ResponseEntity<>(savedClinicalData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // CREATE - Criar dado clínico para um paciente específico
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<ClinicalData> createClinicalDataForPatient(
            @PathVariable("patientId") Long patientId,
            @RequestBody ClinicalData clinicalData) {
        try {
            Optional<Patient> patientData = patientRepository.findById(patientId);
            if (patientData.isPresent()) {
                clinicalData.setPatient(patientData.get());
                ClinicalData savedClinicalData = clinicalDataRepository.save(clinicalData);
                return new ResponseEntity<>(savedClinicalData, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar todos os dados clínicos
    @GetMapping
    public ResponseEntity<List<ClinicalData>> getAllClinicalData() {
        try {
            List<ClinicalData> clinicalDataList = clinicalDataRepository.findAll();
            if (clinicalDataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clinicalDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar dado clínico por ID
    @GetMapping("/{id}")
    public ResponseEntity<ClinicalData> getClinicalDataById(@PathVariable("id") Long id) {
        Optional<ClinicalData> clinicalDataOptional = clinicalDataRepository.findById(id);
        
        if (clinicalDataOptional.isPresent()) {
            return new ResponseEntity<>(clinicalDataOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // READ - Buscar dados clínicos por paciente ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ClinicalData>> getClinicalDataByPatientId(@PathVariable("patientId") Long patientId) {
        try {
            List<ClinicalData> clinicalDataList = clinicalDataRepository.findByPatientId(patientId);
            if (clinicalDataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clinicalDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar dados clínicos por nome do componente
    @GetMapping("/component/{componentName}")
    public ResponseEntity<List<ClinicalData>> getClinicalDataByComponentName(@PathVariable("componentName") String componentName) {
        try {
            List<ClinicalData> clinicalDataList = clinicalDataRepository.findByComponentName(componentName);
            if (clinicalDataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clinicalDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar dados clínicos mais recentes de um paciente
    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<List<ClinicalData>> getLatestClinicalDataByPatientId(@PathVariable("patientId") Long patientId) {
        try {
            List<ClinicalData> clinicalDataList = clinicalDataRepository.findLatestClinicalDataByPatientId(patientId);
            if (clinicalDataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clinicalDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar dados clínicos por paciente e componente
    @GetMapping("/patient/{patientId}/component/{componentName}")
    public ResponseEntity<List<ClinicalData>> getClinicalDataByPatientAndComponent(
            @PathVariable("patientId") Long patientId,
            @PathVariable("componentName") String componentName) {
        try {
            List<ClinicalData> clinicalDataList = clinicalDataRepository.findByPatientIdAndComponentName(patientId, componentName);
            if (clinicalDataList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(clinicalDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Atualizar dado clínico
    @PutMapping("/{id}")
    public ResponseEntity<ClinicalData> updateClinicalData(@PathVariable("id") Long id, @RequestBody ClinicalData clinicalData) {
        Optional<ClinicalData> clinicalDataOptional = clinicalDataRepository.findById(id);
        
        if (clinicalDataOptional.isPresent()) {
            ClinicalData existingClinicalData = clinicalDataOptional.get();
            existingClinicalData.setComponentName(clinicalData.getComponentName());
            existingClinicalData.setComponentValue(clinicalData.getComponentValue());
            existingClinicalData.setMeasuredDateTime(clinicalData.getMeasuredDateTime());
            
            return new ResponseEntity<>(clinicalDataRepository.save(existingClinicalData), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Deletar dado clínico por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteClinicalData(@PathVariable("id") Long id) {
        try {
            if (clinicalDataRepository.existsById(id)) {
                clinicalDataRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Deletar todos os dados clínicos de um paciente
    @DeleteMapping("/patient/{patientId}")
    public ResponseEntity<HttpStatus> deleteClinicalDataByPatientId(@PathVariable("patientId") Long patientId) {
        try {
            clinicalDataRepository.deleteByPatientId(patientId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UTILITY - Contar dados clínicos por paciente
    @GetMapping("/patient/{patientId}/count")
    public ResponseEntity<Long> countClinicalDataByPatientId(@PathVariable("patientId") Long patientId) {
        try {
            long count = clinicalDataRepository.countByPatientId(patientId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // metodo que recebe ID do paciente, clinical data usando DTO e salva no banco
    @PostMapping("/clinicals")
    public ResponseEntity<ClinicalData> saveClinicalData(@RequestBody ClinicalDataDTO clinicalDataDTO) {
        try {
            ClinicalData clinicalData = new ClinicalData();
            clinicalData.setComponentName(clinicalDataDTO.getComponentName());
            clinicalData.setComponentValue(clinicalDataDTO.getComponentValue());

            Patient patient = patientRepository.findById(clinicalDataDTO.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + clinicalDataDTO.getPatientId()));

            clinicalData.setPatient(patient);
            ClinicalData savedClinicalData = clinicalDataRepository.save(clinicalData);
            return new ResponseEntity<>(savedClinicalData, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
