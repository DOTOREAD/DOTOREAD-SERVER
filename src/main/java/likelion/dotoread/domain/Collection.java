package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import likelion.dotoread.domain.mapping.CollectionLike;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Collection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String memo;
    @Builder.Default
    private Integer likeCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<CollectionBookmark> collectionBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL)
    private List<CollectionLike> likeList = new ArrayList<>();
}
