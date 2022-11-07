package com.example.demo.entity.user;

import com.example.demo.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK값으로 활용하는 아이디값

    @Column(nullable = false, unique = true)
    private String username; // 사용자 아이디

    @Column(nullable = false)
    private String password; // 사용자 패스워드

    @Column(nullable = false)
    private String name; // 사용자 이름

    @Column(nullable = false)
    private String subject; // 사용자 학과

    @Column(nullable = false, unique = true)
    private String studentId; // 사용자 학번

    @Enumerated(EnumType.STRING)
    private Authority authority; // 사용자 권한
}
