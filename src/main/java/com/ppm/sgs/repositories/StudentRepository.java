package com.ppm.sgs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ppm.sgs.models.Student;

public interface StudentRepository extends JpaRepository<Student, String> {
    boolean existsByPhone(String phone);

    Optional<Student> findByPhone(String phone);

    Optional<Student> findFirstByOrderByIdDesc();

    @Query("SELECT s FROM Student s WHERE (:id is null or s.id LIKE :id) AND (:name is null or s.name LIKE :name)")
    List<Student> findByIdAndName(String id, String name);
}
