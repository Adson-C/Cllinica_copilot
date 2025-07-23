package com.ads.clinica.clinicaads.repository;

import com.ads.clinica.clinicaads.models.ClinicalData;
import com.ads.clinica.clinicaads.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ClinicalDataRepository extends JpaRepository<ClinicalData, Long> {

    // Buscar dados clínicos por paciente
    List<ClinicalData> findByPatient(Patient patient);

    // Buscar dados clínicos por ID do paciente
    List<ClinicalData> findByPatientId(Long patientId);

    // Buscar dados clínicos por nome do componente
    List<ClinicalData> findByComponentName(String componentName);

    // Buscar dados clínicos por valor do componente
    List<ClinicalData> findByComponentValue(String componentValue);

    // Buscar dados clínicos por nome e valor do componente
    List<ClinicalData> findByComponentNameAndComponentValue(String componentName, String componentValue);

    // Buscar dados clínicos por paciente e nome do componente
    List<ClinicalData> findByPatientAndComponentName(Patient patient, String componentName);

    // Buscar dados clínicos por paciente ID e nome do componente
    List<ClinicalData> findByPatientIdAndComponentName(Long patientId, String componentName);

    // Buscar dados clínicos por intervalo de data
    List<ClinicalData> findByMeasuredDateTimeBetween(Timestamp startDate, Timestamp endDate);

    // Buscar dados clínicos por paciente e intervalo de data
    List<ClinicalData> findByPatientAndMeasuredDateTimeBetween(Patient patient, Timestamp startDate, Timestamp endDate);

    // Query para buscar os dados clínicos mais recentes de um paciente
    @Query("SELECT cd FROM ClinicalData cd WHERE cd.patient.id = :patientId ORDER BY cd.measuredDateTime DESC")
    List<ClinicalData> findLatestClinicalDataByPatientId(@Param("patientId") Long patientId);

    // Query para buscar dados clínicos únicos por componente e paciente
    @Query("SELECT cd FROM ClinicalData cd WHERE cd.patient.id = :patientId AND cd.componentName = :componentName ORDER BY cd.measuredDateTime DESC")
    List<ClinicalData> findByPatientIdAndComponentNameOrderByMeasuredDateTimeDesc(@Param("patientId") Long patientId, @Param("componentName") String componentName);

    // Contar dados clínicos por paciente
    long countByPatient(Patient patient);

    // Contar dados clínicos por ID do paciente
    long countByPatientId(Long patientId);

    // Deletar dados clínicos por paciente
    void deleteByPatient(Patient patient);

    // Deletar dados clínicos por ID do paciente
    void deleteByPatientId(Long patientId);
}
