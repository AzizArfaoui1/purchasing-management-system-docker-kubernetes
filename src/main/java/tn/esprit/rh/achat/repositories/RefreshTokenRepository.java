package tn.esprit.rh.achat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.rh.achat.entities.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
