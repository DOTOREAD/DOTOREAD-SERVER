package likelion.dotoread.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveFolderRequest(
        @NotBlank(message = "name은 필수 항목입니다.")
        String name
) {

}