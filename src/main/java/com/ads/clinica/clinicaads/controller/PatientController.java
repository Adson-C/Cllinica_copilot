package com.ads.clinica.clinicaads.controller;

import com.ads.clinica.clinicaads.models.Patient;
import com.ads.clinica.clinicaads.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // CREATE - Criar um novo paciente
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        try {
            Patient savedPatient = patientRepository.save(patient);
            return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar todos os pacientes
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> patients = patientRepository.findAll();
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar paciente por ID
   @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable("id") Long id) {
        try {
            Optional<Patient> patientData = patientRepository.findById(id);

            if (patientData.isPresent()) {
                return new ResponseEntity<>(patientData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Paciente não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar paciente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar pacientes por primeiro nome
    @GetMapping("/firstname/{firstName}")
    public ResponseEntity<List<Patient>> getPatientsByFirstName(@PathVariable("firstName") String firstName) {
        try {
            List<Patient> patients = patientRepository.findByFirstName(firstName);
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar pacientes por sobrenome
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<List<Patient>> getPatientsByLastName(@PathVariable("lastName") String lastName) {
        try {
            List<Patient> patients = patientRepository.findByLastName(lastName);
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar pacientes por idade
    /**
     * Retorna uma lista de pacientes com a idade especificada.
     *
     * @param age Idade dos pacientes a serem buscados.
     * @return ResponseEntity contendo a lista de pacientes encontrados e o status HTTP correspondente:
     *         - 200 OK: se pacientes forem encontrados,
     *         - 204 NO_CONTENT: se nenhum paciente for encontrado,
     *         - 500 INTERNAL_SERVER_ERROR: em caso de erro interno.
     */
    @GetMapping("/age/{age}")
    public ResponseEntity<List<Patient>> getPatientsByAge(@PathVariable("age") Integer age) {
        try {
            List<Patient> patients = patientRepository.findByAge(age);
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar pacientes por faixa de idade
    @GetMapping("/age/range")
    public ResponseEntity<List<Patient>> getPatientsByAgeRange(
            @RequestParam("min") Integer minAge, 
            @RequestParam("max") Integer maxAge) {
        try {
            List<Patient> patients = patientRepository.findByAgeBetween(minAge, maxAge);
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar paciente por nome completo
    @GetMapping("/fullname/{fullName}")
    public ResponseEntity<List<Patient>> getPatientsByFullName(@PathVariable("fullName") String fullName) {
        try {
            List<Patient> patients = patientRepository.findByFullNameContainingIgnoreCase(fullName);
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Buscar pacientes com dados clínicos
    @GetMapping("/with-clinical-data")
    public ResponseEntity<List<Patient>> getPatientsWithClinicalData() {
        try {
            List<Patient> patients = patientRepository.findPatientsWithClinicalData();
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Atualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        Optional<Patient> patientData = patientRepository.findById(id);
        
        if (patientData.isPresent()) {
            Patient existingPatient = patientData.get();
            existingPatient.setFirstName(patient.getFirstName());
            existingPatient.setLastName(patient.getLastName());
            existingPatient.setAge(patient.getAge());
            
            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE - Atualizar parcialmente um paciente (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Patient> partialUpdatePatient(@PathVariable("id") Long id, @RequestBody Patient patient) {
        Optional<Patient> patientData = patientRepository.findById(id);
        
        if (patientData.isPresent()) {
            Patient existingPatient = patientData.get();
            
            if (patient.getFirstName() != null) {
                existingPatient.setFirstName(patient.getFirstName());
            }
            if (patient.getLastName() != null) {
                existingPatient.setLastName(patient.getLastName());
            }
            if (patient.getAge() != null) {
                existingPatient.setAge(patient.getAge());
            }
            
            return new ResponseEntity<>(patientRepository.save(existingPatient), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE - Deletar paciente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable("id") Long id) {
        try {
            if (patientRepository.existsById(id)) {
                patientRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Deletar todos os pacientes
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllPatients() {
        try {
            patientRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UTILITY - Contar total de pacientes
    @GetMapping("/count")
    public ResponseEntity<Long> countPatients() {
        try {
            long count = patientRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UTILITY - Verificar se paciente existe
    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkPatientExists(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName) {
        try {
            boolean exists = patientRepository.existsByFirstNameAndLastName(firstName, lastName);
            return new ResponseEntity<>(exists, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
