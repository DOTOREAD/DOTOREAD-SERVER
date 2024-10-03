package likelion.dotoread.auth.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.auth.jwt.JWTUtil;
import likelion.dotoread.auth.oauth2.CustomOAuth2User;
import likelion.dotoread.converter.UserConverter;
import likelion.dotoread.domain.RefreshToken;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.RefreshRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.UserDto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        // 액세스 토큰 생성 (1시간 유효)
        String access = jwtUtil.createJwt("access", username, role, 3600000L);
        // 리프레시 토큰 생성 (14일 유효)
        String refresh = jwtUtil.createJwt("refresh", username, role, 1209600000L);
        // 액세스 토큰은 헤더에 설정
        response.setHeader("access", access);

        User user = userRepository.findByUsername(username);
        user.setAccessToken(access);
        userRepository.save(user);

        //Refresh 토큰 저장
        if(refreshRepository.existsByUsername(username)) {
            refreshRepository.deleteByUsername(username);
        }
        addRefreshEntity(username, refresh, 86400000L);

        // 리프레시 토큰은 쿠키에 설정
        response.addCookie(createCookie("refresh", refresh));

        //회원가입인지, 로그인인지 구분
        boolean isNew = userService.isNew(user);
        String refreshToken = userService.findRefreshTokenByUser(user);
        UserResponseDTO.JWTResponseDTO jwtResponseDTO = UserConverter.toJwtResponseDTO(user, refreshToken, isNew);

        //로그인 응답
        ApiResponse<UserResponseDTO.JWTResponseDTO> apiResponse = ApiResponse.of(SuccessStatus._GOOGLE_LOGIN_OK, jwtResponseDTO);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        objectMapper.writeValue(response.getWriter(), apiResponse);

        // 리다이렉트
//        response.sendRedirect("http://localhost:8080");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
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
