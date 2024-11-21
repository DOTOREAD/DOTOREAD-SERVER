package likelion.dotoread.repository;

import likelion.dotoread.domain.mapping.CollectionBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionBookmarkRepository extends JpaRepository<CollectionBookmark, Long> {
}
