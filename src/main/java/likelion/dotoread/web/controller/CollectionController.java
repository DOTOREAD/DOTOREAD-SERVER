package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
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
    @Operation(summary = "새 글 작성 api", description = "새 글을 작성하는 api 입니다.")
    public ApiResponse postCollection(HttpServletRequest http, @RequestBody CollectionRequestDTO.CollectionDTO request) {
        collectionService.createCollection(http, request);
        return ApiResponse.of(SuccessStatus._COLLECTION_CREATE_OK, null);
    }
    @GetMapping("/collections/{collectionId}")
    @Operation(summary = "글 상세 조회 api", description = "하나의 글을 상세 조회하는 api입니다.")
    public ApiResponse<CollectionResponseDTO.CollectionDetailDTO> getDetailCollection(HttpServletRequest http, @PathVariable Long collectionId) {
        CollectionResponseDTO.CollectionDetailDTO response = collectionService.getDetailCollection(http, collectionId);
        return ApiResponse.of(SuccessStatus._GET_COLLECTION_,response);
    }

}
