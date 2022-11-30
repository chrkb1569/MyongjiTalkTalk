package com.example.demo.repository.user;

import com.example.demo.entity.user.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsById(String Id);
}
