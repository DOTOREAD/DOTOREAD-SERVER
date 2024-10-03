package likelion.dotoread.auth.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.auth.jwt.JWTUtil;
import likelion.dotoread.auth.oauth2.CustomOAuth2User;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 access키에 담긴 토큰을 꺼냄
        String accessToken = request.getHeader("access");
        if (request.getRequestURI().equals("/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

// 토큰이 없다면 다음 필터로 넘김
        if (accessToken == null) {

            filterChain.doFilter(request, response);

            return;
        }


// 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            sendErrorResponse(response, ErrorStatus._ACCESS_EXPIRED);
            return;
        }

// 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            sendErrorResponse(response, ErrorStatus._ACCESS_INVALID);
            return;
        }


        //토큰에서 username과 role 획득
         String username = jwtUtil.getUsername(accessToken);
        //String role = jwtUtil.getRole(accessToken);
        User user = userRepository.findByUsername(username);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
    private void sendErrorResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatus.getHttpStatus().value());

        ApiResponse<?> apiResponse = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}