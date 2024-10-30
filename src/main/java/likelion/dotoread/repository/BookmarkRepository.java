package likelion.dotoread.repository;

import likelion.dotoread.domain.Bookmark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUserId(Long userId, Sort sort);
    List<Bookmark> findAllByUserIdAndFolderIsNull(Long userId, Sort sort);
}
