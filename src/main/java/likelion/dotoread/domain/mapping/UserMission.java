package likelion.dotoread.domain.mapping;

import jakarta.persistence.*;
import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.Mission;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.common.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserMission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer current;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;
    @OneToMany(mappedBy = "userMission", cascade = CascadeType.ALL)
    private List<AcornAdd> acornAdds = new ArrayList<>();

    public void setCurrent(){
        this.current++;
    }
    public void resetCurrent(){
        this.current = 0;
    }
}
