package likelion.dotoread.repository;

import likelion.dotoread.domain.Folder;
import likelion.dotoread.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    Folder findByNameAndUser(String name, User user);
}
