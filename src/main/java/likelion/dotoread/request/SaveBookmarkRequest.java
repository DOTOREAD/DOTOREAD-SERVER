package likelion.dotoread.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveBookmarkRequest(
        @NotBlank(message = "URL은 필수 항목입니다.")
        String url,

        @NotNull(message = "사용자 ID는 필수 항목입니다.")
        Long userId
) {

}