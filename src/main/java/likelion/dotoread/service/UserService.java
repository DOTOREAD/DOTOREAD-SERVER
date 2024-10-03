package likelion.dotoread.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.auth.jwt.JWTUtil;
import likelion.dotoread.domain.RefreshToken;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.RefreshRepository;
import likelion.dotoread.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;
    public User findUserByHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader("access");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new UserHandler(ErrorStatus._ACCESS_NOT_FOUND);
        }
        String username = jwtUtil.getUsername(accessToken);
        return userRepository.findByUsername(username);
    }
    public boolean isNew(User user) {
        if(user.getNickname() == null || user.getNickname().isEmpty()) {
            return true;
        }
        else return false;
    }

    public String findRefreshTokenByUser(User user) {
        RefreshToken refreshToken = refreshRepository.findByUsername(user.getUsername());
        return refreshToken.getRefresh();
    }
    public void signUp(User user, String nickname) {
        user.setNickname(nickname);
        userRepository.save(user);
    }
    public String getRefresh(HttpServletRequest request, HttpServletResponse response, String refresh) {
        //get refresh token
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        if (refresh == null) {

            //response status code
            throw new UserHandler(ErrorStatus._REFRESH_NOT_FOUND);
        }
        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            //response status code
            throw new UserHandler(ErrorStatus._REFRESH_EXPIRED);
        }
        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            throw new UserHandler(ErrorStatus._REFRESH_INVALID);
        }
        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            throw new UserHandler(ErrorStatus._REFRESH_INVALID);
        }
        return refresh;
    }
}
