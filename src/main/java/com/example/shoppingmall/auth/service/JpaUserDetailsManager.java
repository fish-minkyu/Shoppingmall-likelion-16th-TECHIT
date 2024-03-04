package com.example.shoppingmall.auth.service;

import com.example.shoppingmall.ImageSort;
import com.example.shoppingmall.MultipartFileFacade;
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

import java.util.Optional;


@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtils jwtTokenUtils;
  private final MultipartFileFacade multipartFileFacade;

  public JpaUserDetailsManager(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    JwtTokenUtils jwtTokenUtils,
    MultipartFileFacade multipartFileFacade
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenUtils = jwtTokenUtils;
    this.multipartFileFacade =  multipartFileFacade;

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
    // 유저 이미지 저장 후 targetUser 반환
    UserEntity targetUser =
      (UserEntity) multipartFileFacade.insertImage(ImageSort.USER, imageFile);

    // 5. UserEntity 다 채웠으면 "INACTIVE" -> "COMMON"으로 변경
    if (targetUser.isNoNull()
      && targetUser.getAuthority().equals(UserAuthority.INACTIVE)) {
      targetUser.setAuthority(UserAuthority.COMMON);
    }

    // 6. 저장 후 반환
    return UserDto.fromEntity(userRepository.save(targetUser));
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