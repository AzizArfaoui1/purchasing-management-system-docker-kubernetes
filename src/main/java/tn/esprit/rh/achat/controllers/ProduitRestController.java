package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.services.IProduitService;

import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin("*")
@Tag(name = "Gestion des produits", description = "APIs pour la gestion des produits")
@RequestMapping("/produit")
public class ProduitRestController {

	@Autowired
	IProduitService produitService;

	// http://localhost:8089/SpringMVC/produit/retrieve-all-produits
	@GetMapping("/retrieve-all-produits")
	@ResponseBody
	@Operation(summary = "Récupérer tous les produits",
			description = "Retourne la liste de tous les produits")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<Produit> getProduits() {
		List<Produit> list = produitService.retrieveAllProduits();
		return list;
	}

	// http://localhost:8089/SpringMVC/produit/retrieve-produit/8
	@GetMapping("/retrieve-produit/{produit-id}")
	@ResponseBody
	@Operation(summary = "Récupérer un produit par ID",
			description = "Retourne un produit spécifique basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produit trouvé"),
			@ApiResponse(responseCode = "404", description = "Produit non trouvé")
	})
	public Produit retrieveProduit(
			@Parameter(description = "ID du produit à récupérer", required = true)
			@PathVariable("produit-id") Long produitId) {
		return produitService.retrieveProduit(produitId);
	}

	/* Ajouter un produit tout en lui affectant la catégorie produit et le stock associés */
	// http://localhost:8089/SpringMVC/produit/add-produit/{idCategorieProduit}/{idStock}
	@PostMapping("/add-produit")
	@ResponseBody
	@Operation(summary = "Ajouter un nouveau produit",
			description = "Crée un nouveau produit dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produit ajouté avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public Produit addProduit(
			@Parameter(description = "Objet produit à ajouter", required = true)
			@RequestBody Produit p) {
		Produit produit = produitService.addProduit(p);
		return produit;
	}

	// http://localhost:8089/SpringMVC/produit/remove-produit/{produit-id}
	@DeleteMapping("/remove-produit/{produit-id}")
	@ResponseBody
	@Operation(summary = "Supprimer un produit",
			description = "Supprime un produit existant basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produit supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Produit non trouvé")
	})
	public void removeProduit(
			@Parameter(description = "ID du produit à supprimer", required = true)
			@PathVariable("produit-id") Long produitId) {
		produitService.deleteProduit(produitId);
	}

	// http://localhost:8089/SpringMVC/produit/modify-produit/{idCategorieProduit}/{idStock}
	@PutMapping("/modify-produit")
	@ResponseBody
	@Operation(summary = "Modifier un produit",
			description = "Met à jour un produit existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Produit non trouvé")
	})
	public Produit modifyProduit(
			@Parameter(description = "Objet produit à modifier", required = true)
			@RequestBody Produit p) {
		return produitService.updateProduit(p);
	}

	/*
	 * Si le responsable magasin souhaite modifier le stock du produit il peut
	 * le faire en l'affectant au stock en question
	 */
	// http://localhost:8089/SpringMVC/produit/assignProduitToStock/1/5
	@PutMapping(value = "/assignProduitToStock/{idProduit}/{idStock}")
	@Operation(summary = "Assigner un produit à un stock",
			description = "Associe un produit existant à un stock existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Produit assigné au stock avec succès"),
			@ApiResponse(responseCode = "404", description = "Produit ou stock non trouvé")
	})
	public void assignProduitToStock(
			@Parameter(description = "ID du produit", required = true)
			@PathVariable("idProduit") Long idProduit,
			@Parameter(description = "ID du stock", required = true)
			@PathVariable("idStock") Long idStock) {
		produitService.assignProduitToStock(idProduit, idStock);
	}

	/*
	 * Revenu Brut d'un produit (qte * prix unitaire de toutes les lignes du
	 * detailFacture du produit envoyé en paramètre )
	 */
	// http://localhost:8089/SpringMVC/produit/getRevenuBrutProduit/1/{startDate}/{endDate}
    /* @GetMapping(value = "/getRevenuBrutProduit/{idProduit}/{startDate}/{endDate}")
    @Operation(summary = "Calculer le revenu brut d'un produit",
               description = "Calcule le revenu brut d'un produit entre deux dates")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Revenu brut calculé avec succès"),
        @ApiResponse(responseCode = "400", description = "Dates invalides"),
        @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    public float getRevenuBrutProduit(
            @Parameter(description = "ID du produit", required = true)
            @PathVariable("idProduit") Long idProduit,
            @Parameter(description = "Date de début (format: yyyy-MM-dd)", required = true)
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "Date de fin (format: yyyy-MM-dd)", required = true)
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return produitService.getRevenuBrutProduit(idProduit, startDate, endDate);
    } */
}