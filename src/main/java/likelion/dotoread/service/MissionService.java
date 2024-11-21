package likelion.dotoread.service;

import likelion.dotoread.converter.AcornConverter;
import likelion.dotoread.converter.MissionConverter;
import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.Mission;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.UserMission;
import likelion.dotoread.repository.AcornAddRepository;
import likelion.dotoread.repository.UserMissionRepository;
import likelion.dotoread.repository.UserRepository;
import likelion.dotoread.web.dto.MissionDto.MissionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {
    private final UserMissionRepository userMissionRepository;
    private final AcornAddRepository acornAddRepository;
    private final UserRepository userRepository;
    public void createUserMissions(User user, List<Mission> missions) {
        if(userMissionRepository.existsByUser(user)){
            return ;
        }
        List<UserMission> userMissions = missions.stream()
                .map(mission -> UserMission.builder()
                        .user(user)
                        .mission(mission)
                        .current(0)
                        .build())
                .collect(Collectors.toList());

        userMissionRepository.saveAll(userMissions);
    }

    public List<MissionResponseDTO.UserMissionDTO> getUserMissions(User user) {
        List<UserMission> userMissionList = userMissionRepository.findAllByUser(user);
        List<MissionResponseDTO.UserMissionDTO> result = MissionConverter.toUserMissionDTOList(userMissionList);
        return result;
    }

    public void missionUpdate(UserMission userMission) {
        if(userMission.getMission().getGoal() == userMission.getCurrent()) {
            User user = userMission.getUser();
            AcornAdd acornAdd = AcornConverter.toAcornAdd(userMission.getCurrent(), userMission);
            user.addAcorn(userMission.getCurrent());
            userMission.resetCurrent();
            userRepository.save(user);
            userMissionRepository.save(userMission);
            acornAddRepository.save(acornAdd);
        }
    }
}
