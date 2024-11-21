package likelion.dotoread.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.converter.BookmarkConverter;
import likelion.dotoread.converter.CollectionConverter;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.CollectionBookmarkRepository;
import likelion.dotoread.repository.CollectionLikeRepository;
import likelion.dotoread.repository.CollectionRepository;
import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CollectionService {
    private final UserService userService;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CollectionLikeRepository collectionLikeRepository;
    private final CollectionBookmarkRepository collectionBookmarkRepository;

    public void createCollection(HttpServletRequest http, CollectionRequestDTO.CollectionDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.save(CollectionConverter.toCollection(user, request));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByIdIn(request.getBookmarkIds());
        List<CollectionBookmark> collectionBookmarkList = bookmarks.stream()
                .map(bookmark -> CollectionConverter.toCollectionBookmark(collection, bookmark)).collect(Collectors.toList());
        collection.setCollectionBookmarks(collectionBookmarkList);
        collectionRepository.save(collection);
    }

    public CollectionResponseDTO.CollectionDetailDTO getDetailCollection(HttpServletRequest http, Long collectionId) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new UserHandler(ErrorStatus._COLLECTION_NOT_FOUND));
        Boolean isLiked = collectionLikeRepository.existsByUserAndCollection(user, collection);
        List<Bookmark> bookmarks = collectionBookmarkRepository.findAllBookmarks(collection);
        List<BookmarkResponseDTO.BookmarkSummaryDTO> bookmarkSummaryDTOList = bookmarks.stream()
                .map(bookmark -> BookmarkConverter.toBookmarkSummaryDTO(bookmark)).collect(Collectors.toList());
        CollectionResponseDTO.CollectionDetailDTO collectionDetailDTO = CollectionConverter.toCollectionDetailDTO(collection,isLiked,bookmarkSummaryDTOList);
        return collectionDetailDTO;
    }

    public CollectionResponseDTO.CollectionPreviewListDTO getCollectionPreviewList(HttpServletRequest http, Integer page) {
        PageRequest pageRequest = PageRequest.of(page-1, 10);
        Page<Collection> collections = collectionRepository.findAll(pageRequest);
        CollectionResponseDTO.CollectionPreviewListDTO collectionPreviewListDTO = CollectionConverter.toCollectionPreviewListDTO(collections);
        return collectionPreviewListDTO;
    }

    public void deleteCollection(HttpServletRequest http, Long collectionId) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new UserHandler(ErrorStatus._COLLECTION_NOT_FOUND));
        if(!collection.getUser().getId().equals(user.getId())) {
            throw new UserHandler(ErrorStatus._COLLECTION_DELETE_REJECT);
        }
        collectionRepository.delete(collection);
    }

}
