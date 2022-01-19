package com.mumulcom.mumulcom.src.user.repository;

import com.mumulcom.mumulcom.src.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
