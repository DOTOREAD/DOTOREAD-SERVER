package likelion.dotoread.web.dto.BookmarkDto;

import likelion.dotoread.dto.FolderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BookmarkResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookmarkDetailListDTO {
        List<BookmarkDetailDTO> bookmarkDetailDTOList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookmarkDetailDTO {
        Long bookmarkId;
        String title;
        String url;
        String img;
        LocalDateTime createdAt;
        FolderDTO folder;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookmarkSummaryDTO {
        Long bookmarkId;
        String title;
        String url;
    }
}