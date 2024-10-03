package likelion.dotoread.web.dto.UserDto;

import lombok.Getter;

public class UserRequestDTO {
    @Getter
    public static class SignUpDTO {
        private String nickname;
    }
}
