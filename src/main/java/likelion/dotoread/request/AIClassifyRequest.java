package likelion.dotoread.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AIClassifyRequest(
        @NotEmpty(message = "북마크가 비어있습니다.")
        List<Long> bookmarkIds,

        @NotNull(message = "사용자 ID는 필수 항목입니다.")
        Long userId
)  {
}
