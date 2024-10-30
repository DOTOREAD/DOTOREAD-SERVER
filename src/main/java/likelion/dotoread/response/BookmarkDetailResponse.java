package likelion.dotoread.response;


import likelion.dotoread.domain.Bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BookmarkDetailResponse(Long bookmarkId, String title,
                                     String url, LocalDateTime createdAt) {
    public static BookmarkDetailResponse of(final Long bookmarkId, final String title, final String url, final LocalDateTime createdAt) {
        return new BookmarkDetailResponse(bookmarkId, title, url, createdAt);
    }

    public static List<BookmarkDetailResponse> from(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDetailResponse(
                        bookmark.getId(),
                        bookmark.getTitle(),
                        bookmark.getUrl(),
                        bookmark.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}