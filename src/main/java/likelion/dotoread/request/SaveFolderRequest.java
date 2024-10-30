package likelion.dotoread.request;

import jakarta.validation.constraints.NotBlank;

public record SaveFolderRequest(
        @NotBlank(message = "name은 필수 항목입니다.")
        String name
) {

}