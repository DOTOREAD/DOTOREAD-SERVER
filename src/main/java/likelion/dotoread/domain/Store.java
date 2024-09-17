package likelion.dotoread.domain;

import jakarta.persistence.*;
import likelion.dotoread.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Integer price;
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<AcornUse> acornUseList = new ArrayList<>();
}
