package likelion.dotoread.response;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Folder;
import likelion.dotoread.dto.FolderDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BookmarkDetailResponse(Long bookmarkId, String title, String url, String img, LocalDateTime createdAt, FolderDTO folder) {
    public static BookmarkDetailResponse of(final Long bookmarkId, final String title, final String url, String img, final LocalDateTime createdAt, Folder folder) {
        return new BookmarkDetailResponse(bookmarkId, title, url, img, createdAt, FolderDTO.from(folder));
    }

    public static List<BookmarkDetailResponse> from(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDetailResponse(
                        bookmark.getId(),
                        bookmark.getTitle(),
                        bookmark.getUrl(),
                        bookmark.getImg(),
                        bookmark.getCreatedAt(),
                        FolderDTO.from(bookmark.getFolder())
                ))
                .collect(Collectors.toList());
    }
}