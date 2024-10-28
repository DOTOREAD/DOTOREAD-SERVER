package likelion.dotoread.response;


import likelion.dotoread.domain.Bookmark;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BookmarkDetailResponse(Long bookmarkId, String name,
                                     String url, LocalDateTime createdAt) {
    public static BookmarkDetailResponse of(final Long bookmarkId, final String name, final String url, final LocalDateTime createdAt) {
        return new BookmarkDetailResponse(bookmarkId, name, url, createdAt);
    }

    public static List<BookmarkDetailResponse> from(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDetailResponse(
                        bookmark.getId(),
                        bookmark.getName(),
                        bookmark.getUrl(),
                        bookmark.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}