package com.pppppp.amadda.user.repository;

import com.pppppp.amadda.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(String userId);

    Optional<User> findByKakaoId(String kakaoId);

}
