package com.training.learningportal.repository;

import com.training.learningportal.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Studentrepository extends JpaRepository<Student,Integer> {
}
