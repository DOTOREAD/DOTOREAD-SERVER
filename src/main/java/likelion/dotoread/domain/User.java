package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.mapping.UserMission;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String accessToken;
    private String username;
    private String role;
    @Builder.Default
    private Integer donated = 0;
    @Builder.Default
    private Integer storageCount = 100;
    @Builder.Default
    private Integer acornCount = 0;
    @Builder.Default
    private Integer bookmark = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bookmark> bookmarkList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AcornUse> acornUseList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMission> userMissionList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AcornAdd> acronAddList = new ArrayList<>();
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }

    public void addAcorn(Integer acorn){
        this.acornCount += acorn;
    }
    public void addDonated(Integer acorn) {
        this.donated += acorn;
    }
    public void useAcorn(Integer acorn) {this.acornCount -= acorn;}
    public void addStorage(Integer storage) {this.storageCount += storage;}
    public void addBookmark(){this.bookmark++;}
    public void minusBookmark(){this.bookmark--;}
}
