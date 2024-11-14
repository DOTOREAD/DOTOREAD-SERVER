package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.domain.User;
import likelion.dotoread.service.StoreService;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.StoreDto.StoreRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class StoreController {
    private final UserService userService;
    private final StoreService storeService;
    @PostMapping("/funds/world-wide-fund")
    @Operation(summary = "World Wide Fund 후원 api", description = "World Wide Fund에 후원하는 api 입니다.")
    public ApiResponse fundWWF(HttpServletRequest http, @RequestBody StoreRequestDTO.UseAcornDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        storeService.acornUse(user, request.getUseAcorn(), 1L);
        return ApiResponse.of(SuccessStatus._FUND_OK,null);
    }
    @PostMapping("/funds/kara")
    @Operation(summary = "World Wide Fund 후원 api", description = "동물 행동권 카라에 후원하는 api 입니다.")
    public ApiResponse fundKARA(HttpServletRequest http, @RequestBody StoreRequestDTO.UseAcornDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        storeService.acornUse(user, request.getUseAcorn(), 2L);
        return ApiResponse.of(SuccessStatus._FUND_OK,null);
    }
    @PostMapping("/stores/storages")
    @Operation(summary = "스토리지 구매 api", description = "도토리 1개로 스토리지 5개를 늘리는 api 입니다.")
    public ApiResponse getStorage(HttpServletRequest http, @RequestBody StoreRequestDTO.UseAcornDTO request) {
        User user = userService.findUserByHttpServletRequest(http);
        storeService.upgradeStorage(user, request.getUseAcorn());
        return ApiResponse.of(SuccessStatus._UPGRADE_STORAGE_OK,null);
    }

}
