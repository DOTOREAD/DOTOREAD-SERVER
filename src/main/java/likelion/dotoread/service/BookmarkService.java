package likelion.dotoread.service;

import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.exception.GeneralException;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.User;
import likelion.dotoread.repository.BookmarkRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.request.SaveBookmarkRequest;
import org.springframework.stereotype.Service;

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
}