package likelion.dotoread.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClassifyRequest(
        @NotEmpty(message = "북마크가 비어있습니다.")
        List<Long> bookmarkIds
)  {
        public List<Long> getBookmarkIds() {
                return bookmarkIds;
        }
}
