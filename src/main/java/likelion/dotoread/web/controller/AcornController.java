package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.domain.User;
import likelion.dotoread.service.AcornService;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.AcornDto.AcornResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class AcornController {
    private final UserService userService;
    private final AcornService acornService;
    @GetMapping("/acorns-add/history")
    @Operation(summary = "도토리 적립 내역 조회 api", description = "도토리 적립 내역을 조회합니다. 페이지 번호 1번이 1페이지 입니다.")
    public ApiResponse<AcornResponseDTO.AcornAddListDTO> getAcornsAdd(HttpServletRequest http, @RequestParam Integer page) {
        User user = userService.findUserByHttpServletRequest(http);
        AcornResponseDTO.AcornAddListDTO response = acornService.getAcornAddHistory(user, page);
        return ApiResponse.of(SuccessStatus._GET_ACORNADD_HISTORY_OK,response);
    }
    @GetMapping("/acorns-use/history")
    @Operation(summary = "도토리 사용 내역 조회 api", description = "도토리 사용 내역을 조회합니다. 페이지 번호 1번이 1페이지 입니다.")
    public ApiResponse<AcornResponseDTO.AcornUsageListDTO> getAcornsUsage(HttpServletRequest http, @RequestParam Integer page) {
        User user = userService.findUserByHttpServletRequest(http);
        AcornResponseDTO.AcornUsageListDTO response = acornService.getAcornUsageHistory(user, page);
        return ApiResponse.of(SuccessStatus._GET_ACORNUSE_HISTORY_OK,response);
    }

}
