package likelion.dotoread.repository;

import likelion.dotoread.domain.AcornUse;
import likelion.dotoread.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcornUseRepository extends JpaRepository<AcornUse, Long> {
    Page<AcornUse> findAllByUser(User user, PageRequest pageRequest);
}
