package com.example.demo.service.user;

import com.example.demo.config.jwt.TokenProvider;
import com.example.demo.dto.security.TokenRequestDto;
import com.example.demo.dto.security.UserSignInRequestDto;
import com.example.demo.dto.security.TokenResponseDto;
import com.example.demo.dto.security.UserSignUpRequestDto;
import com.example.demo.dto.token.RefreshTokenDto;
import com.example.demo.entity.user.Authority;
import com.example.demo.entity.user.User;
import com.example.demo.exeption.user.DuplicateEmailException;
import com.example.demo.exeption.user.DuplicateUsernameException;
import com.example.demo.exeption.user.LoginFailureException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    private final RedisService redisService;

    @Transactional // 회원 가입 로직
    public void singUp(UserSignUpRequestDto requestDto) {

        userDuplicationCheck(requestDto.getUsername(), requestDto.getEmail());

        User user = User.builder()
                .name(requestDto.getName())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .authority(Authority.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Transactional // 로그인 로직
    public TokenResponseDto signIn(UserSignInRequestDto requestDto) {
        User findUser = userRepository.findUserByUsername(requestDto.getUsername())
                .orElseThrow(LoginFailureException::new); // 먼저 입력 ID를 통하여 해당 사용자를 찾음

        if(!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new LoginFailureException(); // 해당 사용자 정보가 가지고 있는 비밀 번호와 입력한 비밀 번호 비교
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.getAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        RefreshTokenDto token = tokenProvider.createToken(authentication);

        redisService.setValues(authentication.getName(), token.getRefreshToken());

        TokenResponseDto responseDto = new TokenResponseDto(token.getOriginToken(), token.getRefreshToken());

        return responseDto;
    }

    @Transactional // 토큰 재발급 로직
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        Authentication authentication = tokenProvider.getAuthentication(requestDto.getOriginToken());

        redisService.validateRefreshToken(authentication.getName(), requestDto.getRefreshToken());

        RefreshTokenDto refreshTokenDto = tokenProvider.createToken(authentication);

        return new TokenResponseDto(refreshTokenDto.getOriginToken(), refreshTokenDto.getRefreshToken());
    }

    // 사용자가 회원 가입을 요청할경우, 이미 가입되어 있는 정보를 입력한 것이 아닌지 확인하는 로직
    public void userDuplicationCheck(String username, String email) {
        if(userRepository.existsUserByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        if(userRepository.existsUserByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
    }
}
