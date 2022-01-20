package com.mumulcom.mumulcom.src.user.service;

import com.github.dozermapper.core.Mapper;
import com.mumulcom.mumulcom.src.user.domain.User;
import com.mumulcom.mumulcom.src.user.dto.UserSignUpData;
import com.mumulcom.mumulcom.src.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

    public UserService(UserRepository userRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    /**
     * 회원가입
     * @param userSignUpData 회원가입 시 필요한 데이터
     * @return User
     */
    public User join(UserSignUpData userSignUpData) {
        User user = mapper.map(userSignUpData, User.class);
        return userRepository.save(user);
    }
}
