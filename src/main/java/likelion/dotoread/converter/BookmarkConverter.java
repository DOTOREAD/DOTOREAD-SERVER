package likelion.dotoread.converter;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.dto.FolderDTO;
import likelion.dotoread.web.dto.BookmarkDto.BookmarkResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class BookmarkConverter {
    public static BookmarkResponseDTO.BookmarkDetailListDTO toBookmarkDetailListDTO(Page<Bookmark> bookmarks) {
        List<BookmarkResponseDTO.BookmarkDetailDTO> bookmarkDetailDTOList = bookmarks.stream()
                .map(BookmarkConverter::toBookmarkDetailDTO)
                .collect(Collectors.toList());
        return BookmarkResponseDTO.BookmarkDetailListDTO.builder()
                .bookmarkDetailDTOList(bookmarkDetailDTOList)
                .listSize(bookmarks.getSize())
                .totalElements(bookmarks.getTotalElements())
                .totalPage(bookmarks.getTotalPages())
                .isLast(bookmarks.isLast())
                .isFirst(bookmarks.isFirst())
                .build();
    }
    public static BookmarkResponseDTO.BookmarkDetailDTO toBookmarkDetailDTO(Bookmark bookmark) {
        FolderDTO folderDTO;
        if(bookmark.getFolder() == null) {
            folderDTO = null;
        }
        else folderDTO = FolderDTO.from(bookmark.getFolder());
        return BookmarkResponseDTO.BookmarkDetailDTO.builder()
                .url(bookmark.getUrl())
                .img(bookmark.getImg())
                .bookmarkId(bookmark.getId())
                .createdAt(bookmark.getCreatedAt())
                .folder(folderDTO)
                .title(bookmark.getTitle())
                .build();
    }
}
