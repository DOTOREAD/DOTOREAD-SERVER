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
    public static UserResponseDTO.StorageDTO toStorageDTO(Integer storages) {
        return UserResponseDTO.StorageDTO.builder()
                .ownStorage(storages)
                .build();
    }
    public static UserResponseDTO.OwnAcornDTO toOwnAcornDTO(Integer acorns) {
        return UserResponseDTO.OwnAcornDTO.builder()
                .ownAcorn(acorns)
                .build();
    }

    public static UserResponseDTO.ReadBookmark toReadBookmark(User user, Integer read) {
        return UserResponseDTO.ReadBookmark.builder()
                .readBookmark(read)
                .bookmark(user.getBookmark())
                .build();
    }
}
