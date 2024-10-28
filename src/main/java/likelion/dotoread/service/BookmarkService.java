package likelion.dotoread.service;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.User;
import likelion.dotoread.enums.SortType;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.SaveBookmarkRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository, UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
    }

    public void deleteBookmark(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));
        bookmarkRepository.delete(bookmark);
    }

    public Long saveBookmark(SaveBookmarkRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Bookmark bookmark = Bookmark.builder()
                .url(request.url())
                .user(user)
                .build();
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return savedBookmark.getId();
    }

    public BookmarkDetailResponse getBookmarkDetail(Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._BOOKMARK_NOT_FOUND));

        bookmark.setVisitedAt(LocalDateTime.now());
        bookmark.setIsVisited(true);

        bookmarkRepository.save(bookmark);

        return BookmarkDetailResponse.of(
                bookmark.getId(),
                bookmark.getName(),
                bookmark.getUrl(),
                bookmark.getCreatedAt()
        );
    }

    public List<BookmarkDetailResponse> getAllBookmarks(Long userId, SortType sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Sort sort = sortType == SortType.ASC ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }

    public List<BookmarkDetailResponse> getUncategorizedBookmarks(Long userId, SortType sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));

        Sort sort = sortType == SortType.ASC ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdAndFolderIsNull(userId, sort);
        return BookmarkDetailResponse.from(bookmarks);
    }
}