package likelion.dotoread.repository;

import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByNameAndUser(String name, User user);

    @Query("SELECT f.name FROM Folder f WHERE f.user = :user")
    List<String> findFolderNamesByUser(@Param("user") User user);

    List<Folder> findByUser(User user);

}
