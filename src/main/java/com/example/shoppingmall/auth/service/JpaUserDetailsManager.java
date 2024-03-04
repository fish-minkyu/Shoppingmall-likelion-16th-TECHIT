package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.auth.dto.SignupDto;
import com.example.shoppingmall.auth.repo.UserRepository;
import com.example.shoppingmall.auth.dto.BusinessApplicationDto;
import com.example.shoppingmall.auth.dto.UserDto;
import com.example.shoppingmall.auth.entity.CustomUserDetails;
import com.example.shoppingmall.auth.entity.UserAuthority;
import com.example.shoppingmall.auth.entity.UserEntity;
import com.example.shoppingmall.auth.jwt.JwtRequestDto;
import com.example.shoppingmall.auth.jwt.JwtResponseDto;
import com.example.shoppingmall.auth.jwt.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtils jwtTokenUtils;

  public JpaUserDetailsManager(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    JwtTokenUtils jwtTokenUtils
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenUtils = jwtTokenUtils;

     if (!userExists("admin")) {
      // 관리자 계정 생성
      createUser(SignupDto.builder()
        .loginId("admin")
        .password("1234")
        .checkPassword("1234")
        .authority(UserAuthority.ADMIN)
        .build()
      );
     }
  }

  // signup user
  public void createUser(SignupDto dto) {
    // 비밀번호 일치여부 확인
    if (!dto.getPassword().equals(dto.getCheckPassword()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    // 이미 존재하는 userId일 때, 에러 반환
    if (this.userExists(dto.getLoginId()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

    try {
      UserEntity newUser = UserEntity.builder()
        .loginId(dto.getLoginId())
        .password(passwordEncoder.encode(dto.getPassword()))
        .build();

      if (dto.getAuthority() != null) {
        newUser.setAuthority(dto.getAuthority());
      } else {
        newUser.setAuthority(UserAuthority.INACTIVE);
      }

      userRepository.save(newUser);
    } catch (Exception e) {
      log.error("Failed Exception: {}", Exception.class);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public UserDto updateUser(
    CustomUserDetails user
  ) throws UsernameNotFoundException {
    // UserEntity 불러오기
    UserEntity targetEntity = getUserEntity();

    // 수정하기
    targetEntity.setUsername(user.getUsername());
    targetEntity.setNickname(user.getNickname());
    targetEntity.setEmail(user.getEmail());
    targetEntity.setAgeRange(user.getAgeRange());
    targetEntity.setPhone(user.getPhone());

    // UserEntity 다 채웠으면 "INACTIVE" -> "COMMON"으로 변경
    if (targetEntity.isNoNull()) {
      targetEntity.setAuthority(UserAuthority.COMMON);
    }

    // 수정사항 저장
    return UserDto.fromEntity(userRepository.save(targetEntity));
  }

  // update - profile 이미지
  public UserDto updateProfileImage(MultipartFile imageFile) {
    // 1. 접속 유저 정보 가져오기
    UserEntity user = getUserEntity();

    // 2. 파일을 어디에 업로드 할건지 결정
    // media/profile/{Date.now}_profile.{확장자}
    String profileDir = "media/profile/";

    // 2-1. (없다면) 폴더를 만들어야 한다.
    try{
      Files.createDirectories(Path.of(profileDir));
    } catch (IOException e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2-2. 실제 파일 이름을 경로와 확장자를 포함하여 만들기
    // {LocalDateTime.now}_profile
    LocalDateTime now = LocalDateTime.now();
    String filename = String.format("%s_profile", now);

    String originalFilename = imageFile.getOriginalFilename();
    // "whale.png" -> {"whale", "png"}
    String[] fileNameSplit = originalFilename.split("\\.");
    // "blue.whale.png" -> {"blue", "whale", "png"}
    String extension = fileNameSplit[fileNameSplit.length -1];
    String profileFilename = filename + "." + extension;

    String profilePath = profileDir + profileFilename;

    // 3. 실제로 해당 위치에 저장
    try {
      // todo 파일 경로 수정
      imageFile.transferTo(Path.of(profilePath));
    } catch (IOException e) {
      log.error("err: {}", e.getMessage());
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 4. User에 해당 profileImage를 저장
    // {serverDomain}/static/
    String serverDomain = "localhost:8080";
    String requestPath = String.format("%s/static/profile/%s", serverDomain, profileFilename);
    log.info(requestPath);
    user.setProfile(requestPath);

    // 5. UserEntity 다 채웠으면 "INACTIVE" -> "COMMON"으로 변경
    if (user.isNoNull()) {
      user.setAuthority(UserAuthority.COMMON);
    }

    // 6. 저장 후 반환
    return UserDto.fromEntity(userRepository.save(user));
  }

  // 일반 유저 -> 사업자 신청
  public String businessApply(BusinessApplicationDto dto) {
    UserEntity targetEntity = getUserEntity();

    targetEntity.setBusinessNumber(dto.getBusinessNumber());
    targetEntity.setAuthority(UserAuthority.PENDING);

    userRepository.save(targetEntity);
    return "Your application has been completed.";
  }

  // login user - JWT 토큰 발행
  public JwtResponseDto issueJwt(
    JwtRequestDto dto
  ) {
    // 1. 사용자가 제공한 userId가 저장된 사용자인지 판단
    if (!userExists(dto.getLoginId()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    CustomUserDetails customUserDetails
      = loadUserByloginId(dto.getLoginId());

    // 2. 비밀번호 대조
    if (!passwordEncoder
      .matches(dto.getPassword(), customUserDetails.getPassword()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

    // 3. JWT 발급
    String jwt = jwtTokenUtils.generateToken(customUserDetails);
    JwtResponseDto response = new JwtResponseDto();
    response.setToken(jwt);
    return response;
  }

  // user 정보 불러오기
  public CustomUserDetails loadUserByloginId(
    String loginId
  ) throws UsernameNotFoundException {
    Optional<UserEntity> optionalUser = userRepository.findByLoginId(loginId);

    if (optionalUser.isEmpty())
      throw new UsernameNotFoundException(loginId);

    UserEntity user = optionalUser.get();

    return CustomUserDetails.builder()
      .id(user.getId())
      .loginId(user.getLoginId())
      .password(user.getPassword())
      .username(user.getUsername())
      .nickname(user.getNickname())
      .email(user.getEmail())
      .ageRange(user.getAgeRange())
      .phone(user.getPhone())
      .profile(user.getProfile())
      .authority(user.getAuthority())
      .build();
  }

  public UserEntity getUserEntity() {
    CustomUserDetails customUserDetails
      = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      return userRepository.findByLoginId(customUserDetails.getLoginId())
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  public boolean userExists(String loginId) {
    return userRepository.existsByLoginId(loginId);
  }

// -------------------------------------------------------------------------------------

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
  }
}