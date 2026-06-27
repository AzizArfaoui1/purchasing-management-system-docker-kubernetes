package tn.esprit.rh.achat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.rh.achat.entities.Fournisseur;

import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    Optional<Fournisseur> findByNom(String nom);

}

