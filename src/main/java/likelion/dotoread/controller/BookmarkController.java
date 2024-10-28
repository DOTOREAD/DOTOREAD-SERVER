package likelion.dotoread.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.request.SaveBookmarkRequest;
import likelion.dotoread.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private static final String BASE_URI = "/api/v1/bookmarks/";

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Operation(summary = "북마크 추가", description = "북마크를 추가할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Void> saveBookmark(
            @RequestBody @Valid SaveBookmarkRequest saveBookmarkRequest
    ) {
        Long bookmarkId = bookmarkService.saveBookmark(saveBookmarkRequest);
        URI location = URI.create(BASE_URI + bookmarkId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "북마크 삭제", description = "북마크를 삭제할 수 있습니다.")
    @DeleteMapping("/{bookmarkIds}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable List<Long> bookmarkIds) {
        bookmarkIds.forEach(bookmarkService::deleteBookmark);
        return ResponseEntity.noContent().build();
    }
}