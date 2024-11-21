package likelion.dotoread.repository;

import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    UserMission findByUserAndMissionId(User user, Long missionId);
    boolean existsByUser(User user);

    @Query("SELECT m FROM UserMission m WHERE m.user = :user")
    List<UserMission> findAllByUser(@Param("user") User user);
}
