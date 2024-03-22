package neo.cookscorner.repository;

import neo.cookscorner.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like>findLikeByUserUserId(Long userId);
}