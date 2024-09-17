package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;
import likelion.dotoread.domain.enums.Rating;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String img;
    private String memo;
    @Builder.Default
    private Integer visitCount = 0;
    @Enumerated(EnumType.STRING)
    private Rating rating;
    private LocalDateTime visitedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;
}
