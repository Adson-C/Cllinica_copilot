package com.ads.clinica.clinicaads.repository;

import com.ads.clinica.clinicaads.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Buscar paciente por primeiro nome
    List<Patient> findByFirstName(String firstName);

    // Buscar paciente por sobrenome
    List<Patient> findByLastName(String lastName);

    // Buscar paciente por primeiro nome e sobrenome
    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

    // Buscar pacientes por idade
    List<Patient> findByAge(Integer age);

    // Buscar pacientes por faixa de idade
    List<Patient> findByAgeBetween(Integer minAge, Integer maxAge);

    // Buscar pacientes por idade maior que
    List<Patient> findByAgeGreaterThan(Integer age);

    // Buscar pacientes por idade menor que
    List<Patient> findByAgeLessThan(Integer age);

    // Query personalizada para buscar pacientes com dados clínicos
    @Query("SELECT DISTINCT p FROM Patient p LEFT JOIN FETCH p.clinicalDataList WHERE p.clinicalDataList IS NOT EMPTY")
    List<Patient> findPatientsWithClinicalData();

    // Query personalizada para buscar paciente por nome completo (case insensitive)
    @Query("SELECT p FROM Patient p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    List<Patient> findByFullNameContainingIgnoreCase(@Param("fullName") String fullName);

    // Contar pacientes por idade
    long countByAge(Integer age);

    // Verificar se existe paciente com nome específico
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    // Buscar os pacientes mais jovens (limite de registros)
    List<Patient> findTop10ByOrderByAgeAsc();

    // Buscar os pacientes mais velhos (limite de registros)
    List<Patient> findTop10ByOrderByAgeDesc();
}
