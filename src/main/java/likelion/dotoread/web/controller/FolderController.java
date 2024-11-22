package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.web.dto.FolderDto.FolderDTO;
import likelion.dotoread.request.SaveFolderRequest;
import likelion.dotoread.service.FolderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/folders")
public class FolderController {

    private final FolderService folderService;
    private static final String BASE_URI = "/api/v1/folders/";

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @Operation(summary = "폴더 가져오기", description = "유저의 모든 폴더를 가져옵니다.")
    @GetMapping
    public ApiResponse<List<FolderDTO>> getFolders(HttpServletRequest http){
        List<FolderDTO> folders = folderService.getFolders(http);
        return ApiResponse.onSuccess(folders);
    }

    @Operation(summary = "폴더 생성하기", description = "폴더를 생성할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Void> saveFolder(
            HttpServletRequest http, @RequestBody @Valid SaveFolderRequest saveFolderRequest
    ) {
        Long folderId = folderService.saveFolder(http, saveFolderRequest);
        URI location = URI.create(BASE_URI + folderId);
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "폴더 삭제하기", description = "폴더를 삭제할 수 있습니다.")
    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long folderId) {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }
}
