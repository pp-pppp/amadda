package com.pppppp.amadda.user.service;

import com.pppppp.amadda.global.entity.exception.RestApiException;
import com.pppppp.amadda.global.entity.exception.errorcode.UserErrorCode;
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

    public User getUserBySeq(Long userSeq) {
        return findUserByUserSeq(userSeq).orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_FOUND));
    }

    public Optional<User> findUserByUserSeq(Long userSeq) {
        return Optional.ofNullable(userSeq).flatMap(userRepository::findByUserSeq);
    }

}
