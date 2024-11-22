package likelion.dotoread.repository;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @Query("SELECT b FROM Collection b WHERE lower(b.title) LIKE lower(concat('%', :search, '%'))")
    Page<Collection> findByTitleContaining(@Param("search") String search, PageRequest pageRequest);
}
