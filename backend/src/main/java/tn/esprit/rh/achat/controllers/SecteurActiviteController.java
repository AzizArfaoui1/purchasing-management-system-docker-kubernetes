package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.services.ISecteurActiviteService;

import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Tag(name = "Gestion des secteurs d'activités", description = "APIs pour la gestion des secteurs d'activités")
@RequestMapping("/secteurActivite")
@CrossOrigin("*")
public class SecteurActiviteController {

	@Autowired
	ISecteurActiviteService secteurActiviteService;

	// http://localhost:8089/SpringMVC/secteurActivite/retrieve-all-secteurActivite
	@GetMapping("/retrieve-all-secteurActivite")
	@ResponseBody
	@Operation(summary = "Récupérer tous les secteurs d'activités",
			description = "Retourne la liste de tous les secteurs d'activités")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<SecteurActivite> getSecteurActivite() {
		List<SecteurActivite> list = secteurActiviteService.retrieveAllSecteurActivite();
		return list;
	}

	// http://localhost:8089/SpringMVC/secteurActivite/retrieve-secteurActivite/8
	@GetMapping("/retrieve-secteurActivite/{secteurActivite-id}")
	@ResponseBody
	@Operation(summary = "Récupérer un secteur d'activité par ID",
			description = "Retourne un secteur d'activité spécifique basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Secteur d'activité trouvé"),
			@ApiResponse(responseCode = "404", description = "Secteur d'activité non trouvé")
	})
	public SecteurActivite retrieveSecteurActivite(
			@Parameter(description = "ID du secteur d'activité à récupérer", required = true)
			@PathVariable("secteurActivite-id") Long secteurActiviteId) {
		return secteurActiviteService.retrieveSecteurActivite(secteurActiviteId);
	}

	// http://localhost:8089/SpringMVC/secteurActivite/add-secteurActivite
	@PostMapping("/add-secteurActivite")
	@ResponseBody
	@Operation(summary = "Ajouter un nouveau secteur d'activité",
			description = "Crée un nouveau secteur d'activité dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Secteur d'activité ajouté avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public SecteurActivite addSecteurActivite(
			@Parameter(description = "Objet secteur d'activité à ajouter", required = true)
			@RequestBody SecteurActivite sa) {
		SecteurActivite secteurActivite = secteurActiviteService.addSecteurActivite(sa);
		return secteurActivite;
	}

	// http://localhost:8089/SpringMVC/secteurActivite/remove-secteurActivite/{secteurActivite-id}
	@DeleteMapping("/remove-secteurActivite/{secteurActivite-id}")
	@ResponseBody
	@Operation(summary = "Supprimer un secteur d'activité",
			description = "Supprime un secteur d'activité existant basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Secteur d'activité supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Secteur d'activité non trouvé")
	})
	public void removeSecteurActivite(
			@Parameter(description = "ID du secteur d'activité à supprimer", required = true)
			@PathVariable("secteurActivite-id") Long secteurActiviteId) {
		secteurActiviteService.deleteSecteurActivite(secteurActiviteId);
	}

	// http://localhost:8089/SpringMVC/secteurActivite/modify-secteurActivite
	@PutMapping("/modify-secteurActivite")
	@ResponseBody
	@Operation(summary = "Modifier un secteur d'activité",
			description = "Met à jour un secteur d'activité existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Secteur d'activité mis à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Secteur d'activité non trouvé")
	})
	public SecteurActivite modifySecteurActivite(
			@Parameter(description = "Objet secteur d'activité à modifier", required = true)
			@RequestBody SecteurActivite secteurActivite) {
		return secteurActiviteService.updateSecteurActivite(secteurActivite);
	}
}