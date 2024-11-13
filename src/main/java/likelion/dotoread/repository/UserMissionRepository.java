package likelion.dotoread.repository;

import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    UserMission findByUserAndMissionId(User user, Long missionId);
    boolean existsByUser(User user);
}
