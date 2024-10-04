package likelion.dotoread.web.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JWTResponseDTO {
        private Boolean isNew; //회원가입(true), 로그인(false)
        private String accessToken;
        private String refreshToken;
    }
}
