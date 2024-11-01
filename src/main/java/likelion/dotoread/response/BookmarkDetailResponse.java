package likelion.dotoread.response;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Folder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import likelion.dotoread.dto.FolderDTO;

public record BookmarkDetailResponse(Long bookmarkId, String title,
                                     String url, String img, String createdAt, FolderDTO folder) {

    public static BookmarkDetailResponse of(final Long bookmarkId, final String title, final String url,
                                            String img, final LocalDateTime createdAt, Folder folder) {
        String formattedDate = createdAt != null ? createdAt.format(DateTimeFormatter.ofPattern("M월 d일")) : null;
        FolderDTO folderDTO = folder != null ? FolderDTO.from(folder) : null;
        return new BookmarkDetailResponse(bookmarkId, title, url, img, formattedDate, folderDTO);
    }

    public static List<BookmarkDetailResponse> from(final List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> BookmarkDetailResponse.of(
                        bookmark.getId(),
                        bookmark.getTitle(),
                        bookmark.getUrl(),
                        bookmark.getImg(),
                        bookmark.getCreatedAt(),
                        bookmark.getFolder()  // Folder 객체를 넘겨 FolderDTO로 변환
                ))
                .collect(Collectors.toList());
    }
}