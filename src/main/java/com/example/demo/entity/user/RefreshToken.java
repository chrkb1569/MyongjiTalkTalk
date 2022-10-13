package com.example.demo.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RefreshToken {

    @Id
    @Column(name = "token_key")
    private String id;

    @Column(name = "token_value")
    private String value;
}
