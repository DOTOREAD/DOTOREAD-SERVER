package likelion.dotoread.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.converter.CollectionConverter;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.CollectionRepository;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CollectionService {
    private final UserService userService;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;

    public void createCollection(HttpServletRequest http, CollectionRequestDTO.CollectionDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.save(CollectionConverter.toCollection(user, request));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByIdIn(request.getBookmarkIds());
        List<CollectionBookmark> collectionBookmarkList = bookmarks.stream()
                .map(bookmark -> CollectionConverter.toCollectionBookmark(collection, bookmark)).collect(Collectors.toList());
        collection.setCollectionBookmarks(collectionBookmarkList);
        collectionRepository.save(collection);
    }
}
