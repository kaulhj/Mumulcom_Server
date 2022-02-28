package com.mumulcom.mumulcom.src.user.repository;

import com.mumulcom.mumulcom.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmailAndStatus(String email, String status);
    boolean existsUserByEmailAndStatus(String email, String status);
    boolean existsUserByNickname(String nickname);
}
