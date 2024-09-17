package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.mapping.UserMission;

@Entity
public class AcornAdd extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer addAcorn;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_mission_id")
    private UserMission userMission;
}
