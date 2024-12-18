package likelion.dotoread.web.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ClassifyRequest(
        @NotEmpty(message = "북마크가 비어있습니다.")
        List<Long> bookmarkIds,
        List<Long> folderIds
)  {
        public List<Long> getBookmarkIds() {
                return bookmarkIds;
        }
        public List<Long> getFolderIds() { return folderIds; }
}
