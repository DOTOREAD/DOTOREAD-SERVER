package likelion.dotoread.repository;

import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import likelion.dotoread.domain.Bookmark;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByUser(User user, Sort sort);
    List<Bookmark> findAllByUserAndFolderIsNull(User user, Sort sort);
    List<Bookmark> findAllByUserAndFolderId(User user, Long folderId, Sort sort);

    @Query("SELECT b.url FROM Bookmark b WHERE b.id = :id")
    String getUrlById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Bookmark b SET b.folder = null WHERE b.id IN :bookmarkIds")
    void removeFolderIdsByBookmarkIds(List<Long> bookmarkIds);

    @Transactional
    @Modifying
    @Query("UPDATE Bookmark b SET b.folder = null WHERE b.id = :bookmarkId")
    void removeFolderIdByBookmarkId(Long bookmarkId);

    @Transactional
    @Modifying
    @Query("UPDATE Bookmark b SET b.folder = :folder WHERE b.id = :bookmarkId")
    void updateFolderIdByBookmarkId(@Param("bookmarkId") Long bookmarkId, @Param("folder") Folder folder);

    @Query("SELECT Count(*) FROM Bookmark b WHERE b.user = :user AND b.isVisited = true")
    Integer countAllByUserAndIsVisited(@Param("user") User user);
    @Query("select b FROM Bookmark b WHERE b.user = :user order by b.createdAt DESC limit 7 ")
    List<Bookmark> findFreshArticle(@Param("user") User user);

    @Query("select b FROM Bookmark b WHERE b.user = :user and b.isVisited = false order by b.createdAt limit 7 ")
    List<Bookmark> findRottenArticle(@Param("user") User user);

    @Query("select b FROM Bookmark b WHERE b.user = :user order by b.createdAt limit 7 ")
    List<Bookmark> findOldArticle(@Param("user") User user);

}