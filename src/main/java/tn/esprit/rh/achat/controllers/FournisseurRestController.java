package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.services.IFournisseurService;

import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Tag(name = "Gestion des fournisseurs", description = "APIs pour la gestion des fournisseurs")
@RequestMapping("/fournisseur")
@CrossOrigin("*")
public class FournisseurRestController {

	@Autowired
	IFournisseurService fournisseurService;

	// http://localhost:8089/SpringMVC/fournisseur/retrieve-all-fournisseurs
	@GetMapping("/retrieve-all-fournisseurs")
	@ResponseBody
	@Operation(summary = "Récupérer tous les fournisseurs",
			description = "Retourne la liste de tous les fournisseurs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<Fournisseur> getFournisseurs() {
		List<Fournisseur> fournisseurs = fournisseurService.retrieveAllFournisseurs();
		return fournisseurs;
	}

	// http://localhost:8089/SpringMVC/fournisseur/retrieve-fournisseur/8
	@GetMapping("/retrieve-fournisseur/{fournisseur-id}")
	@ResponseBody
	@Operation(summary = "Récupérer un fournisseur par ID",
			description = "Retourne un fournisseur spécifique basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fournisseur trouvé"),
			@ApiResponse(responseCode = "404", description = "Fournisseur non trouvé")
	})
	public Fournisseur retrieveFournisseur(
			@Parameter(description = "ID du fournisseur à récupérer", required = true)
			@PathVariable("fournisseur-id") Long fournisseurId) {
		return fournisseurService.retrieveFournisseur(fournisseurId);
	}

	// http://localhost:8089/SpringMVC/fournisseur/add-fournisseur
	@PostMapping("/add-fournisseur")
	@ResponseBody
	@Operation(summary = "Ajouter un nouveau fournisseur",
			description = "Crée un nouveau fournisseur dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fournisseur ajouté avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public Fournisseur addFournisseur(
			@Parameter(description = "Objet fournisseur à ajouter", required = true)
			@RequestBody Fournisseur f) {
		Fournisseur fournisseur = fournisseurService.addFournisseur(f);
		return fournisseur;
	}

	// http://localhost:8089/SpringMVC/fournisseur/remove-fournisseur/{fournisseur-id}
	@DeleteMapping("/remove-fournisseur/{fournisseur-id}")
	@ResponseBody
	@Operation(summary = "Supprimer un fournisseur",
			description = "Supprime un fournisseur existant basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fournisseur supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Fournisseur non trouvé")
	})
	public void removeFournisseur(
			@Parameter(description = "ID du fournisseur à supprimer", required = true)
			@PathVariable("fournisseur-id") Long fournisseurId) {
		fournisseurService.deleteFournisseur(fournisseurId);
	}

	// http://localhost:8089/SpringMVC/fournisseur/modify-fournisseur
	@PutMapping("/modify-fournisseur")
	@ResponseBody
	@Operation(summary = "Modifier un fournisseur",
			description = "Met à jour un fournisseur existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Fournisseur mis à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Fournisseur non trouvé")
	})
	public Fournisseur modifyFournisseur(
			@Parameter(description = "Objet fournisseur à modifier", required = true)
			@RequestBody Fournisseur fournisseur) {
		return fournisseurService.updateFournisseur(fournisseur);
	}

	// http://localhost:8089/SpringMVC/fournisseur/assignSecteurActiviteToFournisseur/1/5
	@PutMapping(value = "/assignSecteurActiviteToFournisseur/{idSecteurActivite}/{idFournisseur}")
	@Operation(summary = "Assigner un secteur d'activité à un fournisseur",
			description = "Associe un secteur d'activité existant à un fournisseur existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Secteur d'activité assigné avec succès"),
			@ApiResponse(responseCode = "404", description = "Secteur d'activité ou fournisseur non trouvé")
	})
	public void assignSecteurActiviteToFournisseur(
			@Parameter(description = "ID du secteur d'activité", required = true)
			@PathVariable("idSecteurActivite") Long idSecteurActivite,
			@Parameter(description = "ID du fournisseur", required = true)
			@PathVariable("idFournisseur") Long idFournisseur) {
		fournisseurService.assignSecteurActiviteToFournisseur(idSecteurActivite, idFournisseur);
	}
}