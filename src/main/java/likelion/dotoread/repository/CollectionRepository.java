package likelion.dotoread.repository;

import likelion.dotoread.domain.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}
