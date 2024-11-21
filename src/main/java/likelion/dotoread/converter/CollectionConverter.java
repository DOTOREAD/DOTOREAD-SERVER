package likelion.dotoread.converter;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;

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
}
