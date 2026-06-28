package tn.esprit.rh.achat.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.rh.achat.entities.Operateur;

import java.util.Optional;

@Repository
public interface OperateurRepository extends CrudRepository<Operateur, Long> {
    Optional<Operateur> findByNom(String nom);

}

