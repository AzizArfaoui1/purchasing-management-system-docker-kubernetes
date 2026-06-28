package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.services.ICategorieProduitService;

import java.util.List;

// Import SpringDoc OpenAPI 3 annotations
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@Tag(name = "Gestion des catégories Produit", description = "APIs pour la gestion des catégories de produits")
@RequestMapping("/categorieProduit")
public class CategorieProduitController {

	@Autowired
	ICategorieProduitService categorieProduitService;

	// http://localhost:8089/SpringMVC/categorieProduit/retrieve-all-categorieProduit
	@GetMapping("/retrieve-all-categorieProduit")
	@ResponseBody
	@Operation(summary = "Récupérer toutes les catégories de produits",
			description = "Retourne la liste de toutes les catégories de produits")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<CategorieProduit> getCategorieProduit() {
		List<CategorieProduit> list = categorieProduitService.retrieveAllCategorieProduits();
		return list;
	}

	// http://localhost:8089/SpringMVC/categorieProduit/retrieve-categorieProduit/8
	@GetMapping("/retrieve-categorieProduit/{categorieProduit-id}")
	@ResponseBody
	@Operation(summary = "Récupérer une catégorie de produit par ID",
			description = "Retourne une catégorie de produit spécifique basée sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
			@ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
	})
	public CategorieProduit retrieveCategorieProduit(
			@Parameter(description = "ID de la catégorie de produit à récupérer", required = true)
			@PathVariable("categorieProduit-id") Long categorieProduitId) {
		return categorieProduitService.retrieveCategorieProduit(categorieProduitId);
	}

	// http://localhost:8089/SpringMVC/categorieProduit/add-categorieProduit
	@PostMapping("/add-categorieProduit")
	@ResponseBody
	@Operation(summary = "Ajouter une nouvelle catégorie de produit",
			description = "Crée une nouvelle catégorie de produit dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Catégorie ajoutée avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public CategorieProduit addCategorieProduit(
			@Parameter(description = "Objet catégorie de produit à ajouter", required = true)
			@RequestBody CategorieProduit cp) {
		CategorieProduit categorieProduit = categorieProduitService.addCategorieProduit(cp);
		return categorieProduit;
	}

	// http://localhost:8089/SpringMVC/categorieProduit/remove-categorieProduit/{categorieProduit-id}
	@DeleteMapping("/remove-categorieProduit/{categorieProduit-id}")
	@ResponseBody
	@Operation(summary = "Supprimer une catégorie de produit",
			description = "Supprime une catégorie de produit existante basée sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Catégorie supprimée avec succès"),
			@ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
	})
	public void removeCategorieProduit(
			@Parameter(description = "ID de la catégorie de produit à supprimer", required = true)
			@PathVariable("categorieProduit-id") Long categorieProduitId) {
		categorieProduitService.deleteCategorieProduit(categorieProduitId);
	}

	// http://localhost:8089/SpringMVC/categorieProduit/modify-categorieProduit
	@PutMapping("/modify-categorieProduit")
	@ResponseBody
	@Operation(summary = "Modifier une catégorie de produit",
			description = "Met à jour une catégorie de produit existante")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Catégorie mise à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
	})
	public CategorieProduit modifyCategorieProduit(
			@Parameter(description = "Objet catégorie de produit à modifier", required = true)
			@RequestBody CategorieProduit categorieProduit) {
		return categorieProduitService.updateCategorieProduit(categorieProduit);
	}
}