package likelion.dotoread.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import likelion.dotoread.api.ApiResponse;
import likelion.dotoread.api.code.status.SuccessStatus;
import likelion.dotoread.domain.User;
import likelion.dotoread.service.MissionService;
import likelion.dotoread.service.UserService;
import likelion.dotoread.web.dto.MissionDto.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/bookmarks")
public class MissionController {
    private final UserService userService;
    private final MissionService missionService;
    @GetMapping("/missions")
    @Operation(summary = "미션 조회", description = "미션과 현재 진행 상황을 조회합니다.")
    public ApiResponse<List<MissionResponseDTO.UserMissionDTO>> getMission(HttpServletRequest http) {
        User user = userService.findUserByHttpServletRequest(http);
        List<MissionResponseDTO.UserMissionDTO> result = missionService.getUserMissions(user);
        return ApiResponse.of(SuccessStatus._MISSION_GET_OK, result);
    }
}
