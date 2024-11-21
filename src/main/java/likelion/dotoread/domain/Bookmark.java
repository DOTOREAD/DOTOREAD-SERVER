package likelion.dotoread.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.enums.Rating;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    private String img;
    private String memo;
    @Enumerated(EnumType.STRING)
    private Rating rating;
    private LocalDateTime visitedAt;
    @Builder.Default
    private Boolean isVisited = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    @JsonIgnore
    private Folder folder;
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
    private List<CollectionBookmark> collectionBookmarks = new ArrayList<>();
}
