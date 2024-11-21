package likelion.dotoread.repository;

import likelion.dotoread.domain.Bookmark;
import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.mapping.CollectionBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CollectionBookmarkRepository extends JpaRepository<CollectionBookmark, Long> {

    @Query("SELECT b.bookmark FROM CollectionBookmark b WHERE b.collection = :collection")
    List<Bookmark> findAllBookmarks(@Param("collection")Collection collection);

    @Modifying
    @Query("DELETE FROM CollectionBookmark WHERE collection = :collection")
    void  deleteAllByCollection(@Param("collection") Collection collection);
}
