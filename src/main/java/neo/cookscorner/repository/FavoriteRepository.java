package neo.cookscorner.repository;

import neo.cookscorner.entities.Favorite;
import neo.cookscorner.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findFavoriteByUserUserId(Long userId);
}