package com.example.demo.service.user;

import com.example.demo.config.jwt.TokenProvider;
import com.example.demo.dto.security.*;
import com.example.demo.dto.token.RefreshTokenDto;
import com.example.demo.dto.user.*;
import com.example.demo.entity.user.Authority;
import com.example.demo.entity.user.RefreshToken;
import com.example.demo.entity.user.User;
import com.example.demo.exeption.jwt.WrongRefreshTokenException;
import com.example.demo.exeption.user.DuplicateStudentIdException;
import com.example.demo.exeption.user.DuplicateUsernameException;
import com.example.demo.exeption.user.LoginFailureException;
import com.example.demo.exeption.user.UserNotFoundException;
import com.example.demo.repository.user.RefreshTokenRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final TokenProvider tokenProvider;

    @Transactional // 회원 가입 로직
    public void singUp(UserSignUpRequestDto requestDto) {

        userDuplicationCheck(requestDto.getUsername(), requestDto.getStudentId());

        User user = User.builder()
                .name(requestDto.getName())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .subject(requestDto.getSubject())
                .authority(Authority.ROLE_USER)
                .studentId(requestDto.getStudentId())
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

        RefreshToken refreshToken = RefreshToken.builder()
                .id(authentication.getName()).value(token.getRefreshToken()).build();

        refreshTokenRepository.save(refreshToken);

        TokenResponseDto responseDto = new TokenResponseDto(token.getOriginToken(), token.getRefreshToken());

        return responseDto;
    }

    @Transactional // 토큰 재발급 로직
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {

        if(!tokenProvider.validateToken(requestDto.getRefreshToken())) {
            throw new WrongRefreshTokenException();
        }

        Authentication authentication = tokenProvider.getAuthentication(requestDto.getOriginToken());

        RefreshToken findItem = refreshTokenRepository
                .findById(authentication.getName()).orElseThrow(() -> new RuntimeException("현재 로그인하지 않은 회원입니다."));

        if(!findItem.getValue().equals(requestDto.getRefreshToken())) {
            throw new RuntimeException("토큰 정보가 일치하지 않습니다.");
        }

        RefreshTokenDto token = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .id(token.getOriginToken())
                .value(token.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return new TokenResponseDto(token.getOriginToken(), token.getRefreshToken());
    }

    @Transactional // 비밀번호 변경 로직
    public void changePassword(UserPasswordChangeRequestDto requestDto) {
        String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User findUser = userRepository.findUserByUsername(loginUser)
                .orElseThrow(UserNotFoundException::new);

        findUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
    }

    @Transactional // 아이디 찾기 로직
    public String searchId(UserIdRequestDto requestDto) {

        User findUser = userRepository.findUserByStudentId(requestDto.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        if(!findUser.getName().equals(requestDto.getName()) || !findUser.getSubject().equals(requestDto.getSubject())) {
            throw new UserNotFoundException();
        }

        return findUser.getUsername();
    }

    @Transactional // 비밀번호 재발급 로직
    public String reIssuePassword(UserPasswordReissueRequestDto requestDto) {
        User findUser = userRepository.
                findUserByUsernameAndStudentId(requestDto.getUsername(), requestDto.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        String originPassword = findUser.getPassword();

        String reIssuePassword = originPassword.replaceAll("\\D", "");

        findUser.setPassword(passwordEncoder.encode(reIssuePassword));

        return reIssuePassword;
    }


    // 사용자가 회원 가입을 요청할경우, 이미 가입되어 있는 정보를 입력한 것이 아닌지 확인하는 로직
    public void userDuplicationCheck(String username, String studentId) {
        if(userRepository.existsUserByUsername(username)) {
            throw new DuplicateUsernameException(username);
        }
        if(userRepository.existsUserByStudentId(studentId)) {
            throw new DuplicateStudentIdException(studentId);
        }
    }
}
