package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.enums.Rating;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private Folder folder;
}
