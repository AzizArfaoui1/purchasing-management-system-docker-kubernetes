package tn.esprit.rh.achat.entities;

import java.io.Serializable;

import jakarta.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Personne implements Serializable {
	private static final long serialVersionUID = 1L;

	private String nom;
	private String prenom;
	private String password;
}

