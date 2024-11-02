package likelion.dotoread.repository;

import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByNameAndUser(String name, User user);

    List<Folder> findByUser(User user);
}
