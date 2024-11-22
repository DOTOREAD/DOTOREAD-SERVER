package likelion.dotoread.service;

import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.converter.BookmarkConverter;
import likelion.dotoread.converter.CollectionConverter;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.domain.mapping.CollectionLike;
import likelion.dotoread.domain.mapping.UserMission;
import likelion.dotoread.repository.*;
import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CollectionService {
    private final UserService userService;
    private final CollectionRepository collectionRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CollectionLikeRepository collectionLikeRepository;
    private final CollectionBookmarkRepository collectionBookmarkRepository;
    private final UserMissionRepository userMissionRepository;
    private final MissionService missionService;

    public void createCollection(HttpServletRequest http, CollectionRequestDTO.CollectionDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.save(CollectionConverter.toCollection(user, request));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByIdIn(request.getBookmarkIds());
        List<CollectionBookmark> collectionBookmarkList = bookmarks.stream()
                .map(bookmark -> CollectionConverter.toCollectionBookmark(collection, bookmark)).collect(Collectors.toList());
        collection.setCollectionBookmarks(collectionBookmarkList);
        collectionRepository.save(collection);
        UserMission userMission = userMissionRepository.findByUserAndMissionId(user, 3L);
        userMission.setCurrent();
        userMissionRepository.save(userMission);
        missionService.missionUpdate(userMission);
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
        if(!collection.getUser().getId().equals(user.getId())) {
            UserMission userMission = userMissionRepository.findByUserAndMissionId(user, 4L);
            userMission.setCurrent();
            userMissionRepository.save(userMission);
            missionService.missionUpdate(userMission);
        }
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
    @Transactional
    public void patchCollection(HttpServletRequest http, Long collectionId, CollectionRequestDTO.CollectionDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new UserHandler(ErrorStatus._COLLECTION_NOT_FOUND));
        if(!collection.getUser().getId().equals(user.getId())) {
            throw new UserHandler(ErrorStatus._COLLECTION_DELETE_REJECT);
        }
        if(request.getBookmarkIds() != null && !request.getBookmarkIds().isEmpty()) {
            collectionBookmarkRepository.deleteAllByCollection(collection);
            List<Bookmark> bookmarks = bookmarkRepository.findAllByIdIn(request.getBookmarkIds());
            List<CollectionBookmark> collectionBookmarkList = bookmarks.stream()
                    .map(bookmark -> CollectionConverter.toCollectionBookmark(collection, bookmark)).collect(Collectors.toList());
            collection.setCollectionBookmarks(collectionBookmarkList);
        }
        if(request.getMemo() != null && !request.getMemo().isEmpty()) {
            collection.setMemo(request.getMemo());
        }
        if(request.getTitle() != null && !request.getTitle().isEmpty()) {
            collection.setTitle(request.getTitle());
        }
        collectionRepository.save(collection);
    }

    public void createCollectionLike(HttpServletRequest http, Long collectionId){
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new UserHandler(ErrorStatus._COLLECTION_NOT_FOUND));

        if (collectionLikeRepository.existsByCollectionAndUser(collection, user)) {
            throw new UserHandler(ErrorStatus._ALREADY_LIKED);
        }

        CollectionLike collectionLike = CollectionLike.builder()
                .collection(collection)
                .user(user)
                .build();
        collectionLikeRepository.save(collectionLike);

        collection.setLikeCount(collection.getLikeCount() + 1);
        collectionRepository.save(collection);
    }

    public void deleteCollectionLike(HttpServletRequest http, Long collectionId){
        User user = userService.findUserByHttpServletRequest(http);
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(()->new UserHandler(ErrorStatus._COLLECTION_NOT_FOUND));
        CollectionLike collectionLike = collectionLikeRepository.findByCollectionAndUser(collection, user);
        collectionLikeRepository.delete(collectionLike);

        collection.setLikeCount(Math.max(0, collection.getLikeCount() - 1));
        collectionRepository.save(collection);
    }

    public CollectionResponseDTO.CollectionPreviewListDTO searchCollection(HttpServletRequest http, String search, Integer page) {
        User user = userService.findUserByHttpServletRequest(http);
        PageRequest pageRequest = PageRequest.of(page-1,10);
        Page<Collection> collections = collectionRepository.findByTitleContaining(search, pageRequest);
        return CollectionConverter.toCollectionPreviewListDTO(collections);
    }

    public void cloneBookmark(HttpServletRequest http, Long bookmarkId) {
        User user = userService.findUserByHttpServletRequest(http);
        Bookmark originalBookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));

        Bookmark clonedBookmark = Bookmark.builder()
                .title(originalBookmark.getTitle())
                .url(originalBookmark.getUrl())
                .img(originalBookmark.getImg())
                .user(user)
                .build();

        bookmarkRepository.save(clonedBookmark);
    }
}
