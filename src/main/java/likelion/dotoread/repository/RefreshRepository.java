package likelion.dotoread.repository;

import likelion.dotoread.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {

    Boolean existsByRefresh(String refresh);
    Boolean existsByUsername(String username);

    @Transactional
    void deleteByRefresh(String refresh);
    @Transactional
    void deleteByUsername(String username);

    RefreshToken findByUsername(String username);
}
