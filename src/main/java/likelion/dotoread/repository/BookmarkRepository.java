package likelion.dotoread.repository;

import likelion.dotoread.domain.Bookmark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUserId(Long userId, Sort sort);
    List<Bookmark> findAllByUserIdAndFolderIsNull(Long userId, Sort sort);

    @Query("SELECT b.url FROM Bookmark b WHERE b.id = :id")
    String getUrlById(Long id);
}
