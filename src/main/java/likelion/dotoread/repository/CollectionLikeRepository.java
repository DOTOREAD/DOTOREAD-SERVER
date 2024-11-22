package likelion.dotoread.repository;

import likelion.dotoread.domain.Collection;
import likelion.dotoread.domain.User;
import likelion.dotoread.domain.mapping.CollectionLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionLikeRepository extends JpaRepository<CollectionLike, Long> {
    Boolean existsByUserAndCollection(User user, Collection collection);

    CollectionLike findByCollectionAndUser(Collection collection, User user);

    Boolean existsByCollectionAndUser(Collection collection, User user);
}
