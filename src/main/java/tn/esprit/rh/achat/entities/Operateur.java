package tn.esprit.rh.achat.entities;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operateur extends Personne {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOperateur;

	@OneToMany
	@JsonIgnore
	private Set<Facture> factures;

	public Operateur(Long idOperateur, String nom, String prenom, String password) {
		super();
		this.idOperateur = idOperateur;
		this.setNom(nom);
		this.setPrenom(prenom);
		this.setPassword(password);
	}
	
	
	
}

