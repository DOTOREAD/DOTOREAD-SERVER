package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.auth.jwt.JWTUtil;
import likelion.dotoread.converter.UserConverter;
import likelion.dotoread.domain.RefreshToken;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.RefreshRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.UserDto.UserRequestDTO;
import likelion.dotoread.web.dto.UserDto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookmarks")
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입(닉네임설정)", description = "닉네임을 설정합니다.")
    public ApiResponse signUp(HttpServletRequest http, @RequestBody UserRequestDTO.SignUpDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        userService.signUp(user, request.getNickname());
        return ApiResponse.of(SuccessStatus._SIGN_UP_OK,null);
    }
    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "refresh 토큰으로 access 토큰을 재발급 받습니다.")
    public ApiResponse<UserResponseDTO.JWTResponseDTO> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = userService.getRefresh(request, response, null);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        //TODO : 토큰 시간 줄이기
        String newAccess = jwtUtil.createJwt("access", username, role, 86400000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(username, newRefresh, 86400000L);
        //response
        response.setHeader("access", newAccess);
        User user = userRepository.findByUsername(username);
        user.setAccessToken(newAccess);
        userRepository.save(user);
        response.addCookie(createCookie("refresh", newRefresh));
        UserResponseDTO.JWTResponseDTO result = UserConverter.toJwtResponseDTO(user,newRefresh, false);
        return ApiResponse.of(SuccessStatus._REFRESH_OK, result);
    }
    @GetMapping("/acorns")
    @Operation(summary = "보유 도토리 개수 조회 api", description = "보유하고 있는 도토리의 총 개수를 조회하는 api 입니다.")
    public ApiResponse<UserResponseDTO.OwnAcornDTO> getAcorns(HttpServletRequest http) {
        User user = userService.findUserByHttpServletRequest(http);
        UserResponseDTO.OwnAcornDTO response = userService.getOwnAcorns(user);
        return ApiResponse.of(SuccessStatus._GET_OWNACORN_OK,response);
    }

    @GetMapping("/storages")
    @Operation(summary = "보유 스토리지 개수 조회 api", description = "보유하고 있는 스토리지 총 개수를 조회하는 api 입니다.")
    public ApiResponse<UserResponseDTO.StorageDTO> getStorages(HttpServletRequest http) {
        User user = userService.findUserByHttpServletRequest(http);
        UserResponseDTO.StorageDTO response = userService.getSotrages(user);
        return ApiResponse.of(SuccessStatus._GET_STORAGE_OK,response);
    }

    //
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        cookie.setAttribute("SameSite", "None");
        return cookie;
    }
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

}
