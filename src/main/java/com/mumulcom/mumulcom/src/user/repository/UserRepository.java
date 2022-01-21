package com.mumulcom.mumulcom.src.user.repository;

import com.mumulcom.mumulcom.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
