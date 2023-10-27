package com.pppppp.amadda.user.service;

import com.pppppp.amadda.user.dto.response.UserReadResponse;
import com.pppppp.amadda.user.entity.User;
import com.pppppp.amadda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findByUserSeq);
    }

}
