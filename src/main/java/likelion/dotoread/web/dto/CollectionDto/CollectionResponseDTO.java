package likelion.dotoread.web.dto.CollectionDto;

import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class CollectionResponseDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CollectionDetailDTO {
        Long collectionId;
        String nickname;
        LocalDate createdAt;
        String title;
        String memo;
        Integer likeCount;
        Boolean isLiked;
        List<BookmarkResponseDTO.BookmarkSummaryDTO> bookmarkSummaryDTOList;
    }

}
