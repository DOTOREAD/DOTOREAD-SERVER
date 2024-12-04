package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.ErrorStatus;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.api.exception.handler.UserHandler;
import likelion.dotoread.service.CollectionService;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
import likelion.dotoread.web.dto.CollectionDto.CollectionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class CollectionController {
    private final CollectionService collectionService;

    @PostMapping("/collections")
    @Operation(summary = "새 글에 북마크 추가 api", description = "새 글을 작성하기 위해 북마크를 추가하는 api 입니다.")
    public ApiResponse postTempCollection(HttpServletRequest http, @RequestBody CollectionRequestDTO.CollectionTempDTO request) {
        Long collectionId = collectionService.createTempCollection(http, request);
        return ApiResponse.of(SuccessStatus._COLLECTION_CREATE_TEMP_OK, collectionId);
    }

    @PatchMapping("/collections/{collectionId}")
    @Operation(summary = "새 글 작성 api", description = "새 글을 작성하는 api 입니다.")
    public ApiResponse postCollection(HttpServletRequest http, @PathVariable Long collectionId, @RequestBody CollectionRequestDTO.CollectionCreateDTO request) {
        collectionService.createCollection(http, collectionId, request);
        return ApiResponse.of(SuccessStatus._COLLECTION_CREATE_OK, null); //update로 바꾸자
    }

    @GetMapping("/collections/{collectionId}")
    @Operation(summary = "글 상세 조회 api", description = "하나의 글을 상세 조회하는 api입니다.")
    public ApiResponse<CollectionResponseDTO.CollectionDetailDTO> getDetailCollection(HttpServletRequest http, @PathVariable Long collectionId) {
        CollectionResponseDTO.CollectionDetailDTO response = collectionService.getDetailCollection(http, collectionId);
        return ApiResponse.of(SuccessStatus._GET_COLLECTION_,response);
    }

    @GetMapping("/collections")
    @Operation(summary = "전체 글(컬렉션) 목록 조회 api", description = "전체 글 목록을 상세 조회하는 api입니다. 페이지 번호를 주세요. 페이지 번호 1번이 1페이지 입니다.")
    public ApiResponse<CollectionResponseDTO.CollectionPreviewListDTO> getCollectionList(HttpServletRequest http, @RequestParam(name = "page") Integer page) {
        CollectionResponseDTO.CollectionPreviewListDTO response = collectionService.getCollectionPreviewList(http, page);
        return ApiResponse.of(SuccessStatus._GET_LIST_COLLECTION_OK, response);
    }

    @DeleteMapping("/collections/{collectionId}")
    @Operation(summary = "글(컬렉션) 삭제 api", description = "하나의 글(컬렉션)을 삭제하는 api입니다.")
    public ApiResponse deleteCollection(HttpServletRequest http, @PathVariable Long collectionId) {
        collectionService.deleteCollection(http, collectionId);
        return ApiResponse.of(SuccessStatus._DELETE_COLLECTION_OK, null);
    }

    @PatchMapping("/collections/patch/{collectionId}")
    @Operation(summary = "글(컬렉션) 수정 api", description = "하나의 글(컬렉션)을 수정하는 api입니다.")
    public ApiResponse patchCollection(HttpServletRequest http, @PathVariable Long collectionId, @RequestBody CollectionRequestDTO.CollectionDTO request) {
        collectionService.patchCollection(http,collectionId, request);
        return ApiResponse.of(SuccessStatus._PATCH_COLLECTION_OK, null);
    }

    @PostMapping("/collections/like/{collectionId}")
    @Operation(summary = "글(컬렉션) 좋아요 api", description = "글(컬렉션)에 좋아요를 누를 수 있습니다.")
    public ApiResponse postCollectionLike(HttpServletRequest http, @PathVariable Long collectionId) {
        collectionService.createCollectionLike(http, collectionId);
        return ApiResponse.of(SuccessStatus._POST_COLLECTION_LIKE_OK, null);
    }
    @DeleteMapping("/collections/like/{collectionId}")
    @Operation(summary = "글(컬렉션) 좋아요 취소 api", description = "글(컬렉션)에 좋아요를 취소할 수 있습니다.")
    public ApiResponse deleteCollectionLike(HttpServletRequest http, @PathVariable Long collectionId) {
        collectionService.deleteCollectionLike(http, collectionId);
        return ApiResponse.of(SuccessStatus._DELETE_COLLECTION_LIKE_OK, null);
    }

    @Operation(summary = "글(컬렉션) 검색 api", description = "컬렉션 검색 api 입니다. 검색 범위는 컬렉션 제목이며, 페이지 번호 1번이 1페이지입니다.")
    @GetMapping("/collections/search")
    public ApiResponse<CollectionResponseDTO.CollectionPreviewListDTO> searchCollection(@PathParam("search") String search, @PathParam("page") Integer page){
        if(search == null) {
            throw new UserHandler(ErrorStatus._SEARCH_NONE);
        }
        CollectionResponseDTO.CollectionPreviewListDTO response = collectionService.searchCollection(search, page);
        return ApiResponse.of(SuccessStatus._SEARCH_COLLECTION_OK, response);
    }

    @Operation(summary = "글(컬렉션) 내 북마크로 옮기기 api", description = "다른 사람의 글(컬렉션)에 있는 북마크를 내 북마크로 옮길 수 있습니다.")
    @PostMapping("/collections/clone/{bookmarkId}")
    public ApiResponse cloneBookmark(HttpServletRequest http, @PathVariable Long bookmarkId){
        collectionService.cloneBookmark(http, bookmarkId);
        return ApiResponse.of(SuccessStatus._CLONE_COLLECTION_OK, null);
    }
}
