package likelion.dotoread.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.enums.SortType;
import likelion.dotoread.request.SaveBookmarkRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
import likelion.dotoread.service.BookmarkService;
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

    @Operation(summary = "북마크 정보 가져오기", description = "하나의 북마크 정보를 가져옵니다.")
    @GetMapping("/{bookmarkId}")
    public ApiResponse<BookmarkDetailResponse> getBookmarkDetail(HttpServletRequest http, @PathVariable Long bookmarkId){
        BookmarkDetailResponse bookmarkDetail = bookmarkService.getBookmarkDetail(http, bookmarkId);
        return ApiResponse.onSuccess(bookmarkDetail);
    }

    @Operation(summary = "북마크 리스트 가져오기(모든 북마크)", description = "사용자의 모든 북마크를 가져옵니다.")
    @GetMapping("/all")
    public ApiResponse<List<BookmarkDetailResponse>> getAllBookmarks(HttpServletRequest http, @RequestParam(defaultValue = "DESC") SortType sortType){
        List<BookmarkDetailResponse> bookmarkDetail = bookmarkService.getAllBookmarks(http, sortType);
        return ApiResponse.onSuccess(bookmarkDetail);
    }

    @Operation(summary = "폴더에 있는 북마크 리스트 가져오기", description = "특정 폴더 안에 있는 북마크를 가져옵니다.")
    @GetMapping("/all/{folderId}")
    public ApiResponse<List<BookmarkDetailResponse>> getBookmarksInFolder(HttpServletRequest http, @PathVariable Long folderId,
                                                                     @RequestParam(defaultValue = "DESC") SortType sortType){
        List<BookmarkDetailResponse> bookmarkDetail = bookmarkService.getBookmarksInFolder(http, folderId, sortType);
        return ApiResponse.onSuccess(bookmarkDetail);
    }

    @Operation(summary = "북마크 리스트 가져오기(분류 X)", description = "사용자의 분류되지 않은 모든 북마크를 가져옵니다.")
    @GetMapping("/uncategorized")
    public ApiResponse<List<BookmarkDetailResponse>> getUncategorizedBookmarks(HttpServletRequest http,
                                                                     @RequestParam(defaultValue = "DESC") SortType sortType){
        List<BookmarkDetailResponse> bookmarkDetail = bookmarkService.getUncategorizedBookmarks(http, sortType);
        return ApiResponse.onSuccess(bookmarkDetail);
    }

    @Operation(summary = "북마크 추가", description = "북마크를 추가할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Void> saveBookmark(
            HttpServletRequest http, @RequestBody @Valid SaveBookmarkRequest saveBookmarkRequest
    ) {
        Long bookmarkId = bookmarkService.saveBookmark(http, saveBookmarkRequest);
        URI location = URI.create(BASE_URI + bookmarkId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "북마크 삭제", description = "북마크를 삭제할 수 있습니다.")
    @DeleteMapping("/{bookmarkIds}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable List<Long> bookmarkIds) {
        bookmarkIds.forEach(bookmarkService::deleteBookmark);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "fresh-article 조회 api", description = "fresh-article를 조회합니다. 최대 7개입니다.")
    @GetMapping("/fresh-articles")
    public ApiResponse<List<BookmarkDetailResponse>> getFresh(HttpServletRequest http){
        List<BookmarkDetailResponse> response = bookmarkService.getFreshArticle(http);
        return ApiResponse.of(SuccessStatus._GET_FRESH_OK, response);
    }
}