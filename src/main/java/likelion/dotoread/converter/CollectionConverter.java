package likelion.dotoread.converter;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class CollectionConverter {
    public static Collection toTempCollection(User user, CollectionRequestDTO.CollectionTempDTO request) {
        return Collection.builder()
                .title(null)
                .user(user)
                .memo(null)
                .build();
    }

    public static Collection toCollection(User user, CollectionRequestDTO.CollectionDTO request) {
        return Collection.builder()
                .title(request.getTitle())
                .user(user)
                .memo(request.getMemo())
                .build();
    }

    public static void updateCollectionFromDTO(Collection collection, CollectionRequestDTO.CollectionCreateDTO request) {
            collection.setTitle(request.getTitle());
            collection.setMemo(request.getMemo());
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

    public static CollectionResponseDTO.CollectionPreviewDTO toCollectionPreviewDTO(Collection collection, List<BookmarkResponseDTO.BookmarkSummaryDTO> bookmarkSummaryDTOList) {
        return CollectionResponseDTO.CollectionPreviewDTO.builder()
                .collectionId(collection.getId())
                .nickname(collection.getUser().getNickname())
                .createdAt(collection.getCreatedAt().toLocalDate())
                .title(collection.getTitle())
                .memo(collection.getMemo())
                .bookmarkSummaryDTOList(bookmarkSummaryDTOList)
                .build();
    }

    public static CollectionResponseDTO.CollectionPreviewListDTO toCollectionPreviewListDTO(Page<Collection> collections) {
        List<CollectionResponseDTO.CollectionPreviewDTO> collectionPreviewDTOList = collections.stream()
                .map(collection -> {
                    List<BookmarkResponseDTO.BookmarkSummaryDTO> bookmarkSummaryDTOList = collection.getCollectionBookmarks().stream()
                            .map(collectionBookmark -> BookmarkConverter.toBookmarkSummaryDTO(collectionBookmark.getBookmark()))
                            .collect(Collectors.toList());
                    return toCollectionPreviewDTO(collection, bookmarkSummaryDTOList);
                })
                .collect(Collectors.toList());
        return CollectionResponseDTO.CollectionPreviewListDTO.builder()
                .collectionPreviewDTOList(collectionPreviewDTOList)
                .listSize(collections.getSize())
                .totalElements(collections.getTotalElements())
                .isFirst(collections.isFirst())
                .isLast(collections.isLast())
                .totalPage(collections.getTotalPages())
                .build();
    }
}
