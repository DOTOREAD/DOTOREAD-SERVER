package likelion.dotoread.converter;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionResponseDTO;

import java.util.List;

public class CollectionConverter {
    public static Collection toCollection(User user, CollectionRequestDTO.CollectionDTO request) {
        return Collection.builder()
                .title(request.getTitle())
                .user(user)
                .memo(request.getMemo())
                .build();
    }
    public static CollectionBookmark toCollectionBookmark(Collection collection, Bookmark bookmark) {
        return CollectionBookmark.builder()
                .bookmark(bookmark)
                .collection(collection)
                .build();

    }

    public static CollectionResponseDTO.CollectionDetailDTO toCollectionDetailDTO(Collection collection, Boolean isLiked, List<BookmarkResponseDTO.BookmarkSummaryDTO> bookmarkSummaryDTOList) {
        return CollectionResponseDTO.CollectionDetailDTO.builder()
                .nickname(collection.getUser().getNickname())
                .collectionId(collection.getId())
                .createdAt(collection.getCreatedAt().toLocalDate())
                .title(collection.getTitle())
                .memo(collection.getMemo())
                .likeCount(collection.getLikeCount())
                .isLiked(isLiked)
                .bookmarkSummaryDTOList(bookmarkSummaryDTOList)
                .build();
    }
}
