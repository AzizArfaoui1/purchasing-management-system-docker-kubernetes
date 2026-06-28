package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.services.IOperateurService;

import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Gestion des opérateurs", description = "APIs pour la gestion des opérateurs")
@RequestMapping("/operateur")
@CrossOrigin("*")
public class OperateurController {

	@Autowired
	IOperateurService operateurService;

	// http://localhost:8089/SpringMVC/operateur/retrieve-all-operateurs
	@GetMapping("/retrieve-all-operateurs")
	@ResponseBody
	@Operation(summary = "Récupérer tous les opérateurs",
			description = "Retourne la liste de tous les opérateurs")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<Operateur> getOperateurs() {
		List<Operateur> list = operateurService.retrieveAllOperateurs();
		return list;
	}

	// http://localhost:8089/SpringMVC/operateur/retrieve-operateur/8
	@GetMapping("/retrieve-operateur/{operateur-id}")
	@ResponseBody
	@Operation(summary = "Récupérer un opérateur par ID",
			description = "Retourne un opérateur spécifique basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opérateur trouvé"),
			@ApiResponse(responseCode = "404", description = "Opérateur non trouvé")
	})
	public Operateur retrieveOperateur(
			@Parameter(description = "ID de l'opérateur à récupérer", required = true)
			@PathVariable("operateur-id") Long operateurId) {
		return operateurService.retrieveOperateur(operateurId);
	}

	// http://localhost:8089/SpringMVC/operateur/add-operateur
	@PostMapping("/add-operateur")
	@ResponseBody
	@Operation(summary = "Ajouter un nouvel opérateur",
			description = "Crée un nouvel opérateur dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opérateur ajouté avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public Operateur addOperateur(
			@Parameter(description = "Objet opérateur à ajouter", required = true)
			@RequestBody Operateur op) {
		Operateur operateur = operateurService.addOperateur(op);
		return operateur;
	}

	// http://localhost:8089/SpringMVC/operateur/remove-operateur/{operateur-id}
	@DeleteMapping("/remove-operateur/{operateur-id}")
	@ResponseBody
	@Operation(summary = "Supprimer un opérateur",
			description = "Supprime un opérateur existant basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opérateur supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Opérateur non trouvé")
	})
	public void removeOperateur(
			@Parameter(description = "ID de l'opérateur à supprimer", required = true)
			@PathVariable("operateur-id") Long operateurId) {
		operateurService.deleteOperateur(operateurId);
	}

	// http://localhost:8089/SpringMVC/operateur/modify-operateur
	@PutMapping("/modify-operateur")
	@ResponseBody
	@Operation(summary = "Modifier un opérateur",
			description = "Met à jour un opérateur existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Opérateur mis à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Opérateur non trouvé")
	})
	public Operateur modifyOperateur(
			@Parameter(description = "Objet opérateur à modifier", required = true)
			@RequestBody Operateur operateur) {
		return operateurService.updateOperateur(operateur);
	}
}