package com.gtel.homework.repository;

import com.gtel.homework.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByPhoneNumber(String phoneNumber);

}
