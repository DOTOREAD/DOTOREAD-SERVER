package likelion.dotoread.repository;

import likelion.dotoread.domain.AcornAdd;
import likelion.dotoread.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcornAddRepository extends JpaRepository<AcornAdd, Long> {
    Page<AcornAdd> findAllByUser(User user, PageRequest pageRequest);
}
