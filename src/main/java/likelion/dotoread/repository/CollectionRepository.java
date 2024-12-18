package likelion.dotoread.repository;

import likelion.dotoread.domain.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
//    @Query("SELECT b FROM Collection b WHERE lower(b.title) LIKE lower(concat('%', :search, '%'))")
//    Page<Collection> findByTitleContaining(@Param("search") String search, PageRequest pageRequest);
    @Query("SELECT b FROM Collection b WHERE lower(b.title) LIKE lower(concat('%', :search, '%'))")
    List<Collection> findByTitleContaining(@Param("search") String search);
    @Query("SELECT c FROM Collection c ORDER BY c.createdAt DESC")
    List<Collection> findAllByDESC();
}
