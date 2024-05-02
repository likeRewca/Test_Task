package com.task_cs.api.repository;

import com.task_cs.api.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByBirthdayBetween(LocalDate startAge, LocalDate endAge);
}
