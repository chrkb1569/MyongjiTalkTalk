package com.example.demo.repository;

import com.example.demo.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    boolean existsUserByUsername(String username); // 해당 아이디로 가입한 사용자가 있는지 탐색
    boolean existsUserByEmail(String email); // 해당 이메일로 가입한 사용자가 있는지 탐색
}
