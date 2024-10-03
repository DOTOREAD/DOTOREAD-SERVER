package likelion.dotoread.web.controller;

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
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    @PostMapping("/sign-up")
    public ApiResponse signUp(HttpServletRequest http, @RequestBody UserRequestDTO.SignUpDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        userService.signUp(user, request.getNickname());
        return ApiResponse.of(SuccessStatus._SIGN_UP_OK,null);
    }
    @PostMapping("/reissue")
    public ApiResponse<UserResponseDTO.JWTResponseDTO> reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = userService.getRefresh(request, response, null);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //make new JWT
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
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
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

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
