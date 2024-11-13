package likelion.dotoread.converter;

import likelion.dotoread.domain.User;
import likelion.dotoread.web.dto.UserDto.UserResponseDTO;

public class UserConverter {
    public static UserResponseDTO.JWTResponseDTO toJwtResponseDTO(User user, String refreshToken, Boolean isNew) {
        return UserResponseDTO.JWTResponseDTO.builder()
                .refreshToken(refreshToken)
                .accessToken(user.getAccessToken())
                .isNew(isNew)
                .build();
    }
    public static UserResponseDTO.JWTResponseDTO toLoginCheck(boolean isNew, String accessToken, String refreshToken) {
        return UserResponseDTO.JWTResponseDTO.builder()
                .refreshToken(refreshToken)
                .isNew(isNew)
                .accessToken(accessToken)
                .build();
    }
}
