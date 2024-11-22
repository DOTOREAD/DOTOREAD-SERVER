package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.request.ClassifyRequest;
import likelion.dotoread.response.BookmarkDetailResponse;
import likelion.dotoread.service.BookmarkService;
import likelion.dotoread.service.ClassifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/classify")
public class ClassifyController {

    private final BookmarkService bookmarkService;
    private final ClassifyService classifyService;
    private static final String BASE_URI = "/api/v1/classify/";

    public ClassifyController(BookmarkService bookmarkService, ClassifyService classifyService) {
        this.bookmarkService = bookmarkService;
        this.classifyService = classifyService;
    }

    @Operation(summary = "북마크 ai 분류하기", description = "북마크를 ai 분류합니다.")
    @PostMapping("/ai")
    public ApiResponse<List<BookmarkDetailResponse>> AIClassify(HttpServletRequest http, @RequestBody @Valid ClassifyRequest classifyRequest){
        List<BookmarkDetailResponse> classifiedBookmarks = classifyService.AIClassify(http, classifyRequest);
        return ApiResponse.onSuccess(classifiedBookmarks);
    }

    @Operation(summary = "북마크 ai 분류하기 삭제", description = "ai 분류된 북마크를 사용자가 삭제합니다.")
    @PatchMapping("/delete/{bookmarkId}")
    public ResponseEntity<Void> deleteClassify(@PathVariable Long bookmarkId){
        classifyService.deleteClassify(bookmarkId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "북마크 ai 분류하기 취소", description = "ai 분류된 북마크를 전부 취소합니다.")
    @PatchMapping("/cancel")
    public ResponseEntity<Void> cancelClassify(@RequestBody @Valid ClassifyRequest classifyRequest){
        classifyService.cancelClassify(classifyRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "북마크 분류 수정", description = "분류된 북마크를 사용자가 수정합니다.")
    @PatchMapping("/patch/{bookmarkId}/{folderId}")
    public ResponseEntity<Void> patchClassify(@PathVariable Long bookmarkId, @PathVariable Long folderId){
        classifyService.patchClassify(bookmarkId, folderId);
        return ResponseEntity.noContent().build();
    }
}
