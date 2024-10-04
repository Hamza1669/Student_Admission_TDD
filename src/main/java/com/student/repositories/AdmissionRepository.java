package com.student.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.model.Admission;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {

}
