package likelion.dotoread.repository;

import likelion.dotoread.domain.Folder;
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

    List<Bookmark> findAllByUserId(Long userId, Sort sort);
    List<Bookmark> findAllByUserIdAndFolderIsNull(Long userId, Sort sort);
    List<Bookmark> findAllByUserIdAndFolderId(Long userId, Long folderId, Sort sort);

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
}