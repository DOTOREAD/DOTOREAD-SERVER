package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.UserDto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    @GetMapping("/login-check")
    @Operation(summary = "로그인 정보(isNew,토큰) 확인", description = "쿠키에 설정된 값을 기반으로 로그인 정보를 조회합니다.")
    public ApiResponse<UserResponseDTO.JWTResponseDTO> checkLoginStatus(
            @CookieValue(name = "access", required = false) String accessToken) {

        if (accessToken == null) {
            throw new UserHandler(ErrorStatus._ACCESS_NOT_FOUND);
        }

        UserResponseDTO.JWTResponseDTO result = userService.getLoginInfo(accessToken);

        return ApiResponse.of(SuccessStatus._GOOGLE_LOGIN_OK, result);
    }
}
