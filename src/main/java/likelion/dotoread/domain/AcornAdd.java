package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.mapping.UserMission;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AcornAdd extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer addAcorn;
    private LocalDateTime addedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_mission_id")
    private UserMission userMission;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
