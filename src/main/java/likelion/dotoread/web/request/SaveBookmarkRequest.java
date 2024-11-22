package likelion.dotoread.web.request;

import jakarta.validation.constraints.NotBlank;

public record SaveBookmarkRequest(
        @NotBlank(message = "URL은 필수 항목입니다.")
        String url
) {

}