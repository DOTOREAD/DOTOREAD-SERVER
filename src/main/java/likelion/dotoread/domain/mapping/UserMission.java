package likelion.dotoread.domain.mapping;

import jakarta.persistence.*;
import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.Mission;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.common.BaseEntity;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
public class UserMission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime completedAt;
    @Builder.Default
    private Integer current = 0; //현재진행횟수
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;
    @OneToOne(mappedBy = "userMission", cascade = CascadeType.ALL)
    private AcornAdd acornAdd;
}
