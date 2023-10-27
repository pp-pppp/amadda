package com.pppppp.amadda.user.repository;

import com.pppppp.amadda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserSeq(Long userSeq);

}
