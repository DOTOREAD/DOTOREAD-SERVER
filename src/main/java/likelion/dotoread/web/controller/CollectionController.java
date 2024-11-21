package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.service.CollectionService;
import likelion.dotoread.web.dto.CollectionDto.CollectionRequestDTO;
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
}
