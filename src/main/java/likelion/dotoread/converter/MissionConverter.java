package likelion.dotoread.converter;

import likelion.dotoread.domain.mapping.UserMission;
import likelion.dotoread.web.dto.MissionDto.MissionResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MissionConverter {
    public static MissionResponseDTO.UserMissionDTO toUserMissionDTO(UserMission userMission) {
        return MissionResponseDTO.UserMissionDTO.builder()
                .content(userMission.getMission().getContent())
                .goal(userMission.getMission().getGoal())
                .current(userMission.getCurrent())
                .build();
    }

    public static List<MissionResponseDTO.UserMissionDTO> toUserMissionDTOList(List<UserMission> userMissionList) {
        return userMissionList.stream()
                .map(MissionConverter::toUserMissionDTO)
                .collect(Collectors.toList());
    }
}
