package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.services.IStockService;

import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Tag(name = "Gestion des stocks", description = "APIs pour la gestion des stocks")
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockRestController {

	@Autowired
	IStockService stockService;

	// http://localhost:8089/SpringMVC/stock/retrieve-all-stocks
	@GetMapping("/retrieve-all-stocks")
	@ResponseBody
	@Operation(summary = "Récupérer tous les stocks",
			description = "Retourne la liste de tous les stocks")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
	})
	public List<Stock> getStocks() {
		List<Stock> list = stockService.retrieveAllStocks();
		return list;
	}

	// http://localhost:8089/SpringMVC/stock/retrieve-stock/8
	@GetMapping("/retrieve-stock/{stock-id}")
	@ResponseBody
	@Operation(summary = "Récupérer un stock par ID",
			description = "Retourne un stock spécifique basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stock trouvé"),
			@ApiResponse(responseCode = "404", description = "Stock non trouvé")
	})
	public Stock retrieveStock(
			@Parameter(description = "ID du stock à récupérer", required = true)
			@PathVariable("stock-id") Long stockId) {
		return stockService.retrieveStock(stockId);
	}

	// http://localhost:8089/SpringMVC/stock/add-stock
	@PostMapping("/add-stock")
	@ResponseBody
	@Operation(summary = "Ajouter un nouveau stock",
			description = "Crée un nouveau stock dans la base de données")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stock ajouté avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides")
	})
	public Stock addStock(
			@Parameter(description = "Objet stock à ajouter", required = true)
			@RequestBody Stock s) {
		Stock stock = stockService.addStock(s);
		return stock;
	}

	// http://localhost:8089/SpringMVC/stock/remove-stock/{stock-id}
	@DeleteMapping("/remove-stock/{stock-id}")
	@ResponseBody
	@Operation(summary = "Supprimer un stock",
			description = "Supprime un stock existant basé sur son ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stock supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Stock non trouvé")
	})
	public void removeStock(
			@Parameter(description = "ID du stock à supprimer", required = true)
			@PathVariable("stock-id") Long stockId) {
		stockService.deleteStock(stockId);
	}

	// http://localhost:8089/SpringMVC/stock/modify-stock
	@PutMapping("/modify-stock")
	@ResponseBody
	@Operation(summary = "Modifier un stock",
			description = "Met à jour un stock existant")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stock mis à jour avec succès"),
			@ApiResponse(responseCode = "400", description = "Données invalides"),
			@ApiResponse(responseCode = "404", description = "Stock non trouvé")
	})
	public Stock modifyStock(
			@Parameter(description = "Objet stock à modifier", required = true)
			@RequestBody Stock stock) {
		return stockService.updateStock(stock);
	}

	/*
	 * Spring Scheduler : Comparer QteMin tolérée (à ne pas dépasser) avec
	 * Quantité du stock et afficher sur console la liste des produits inférieur
	 * au stock La fct schédulé doit obligatoirement être sans paramètres et
	 * sans retour (void)
	 */
	// http://localhost:8089/SpringMVC/stock/retrieveStatusStock
	// @Scheduled(fixedRate = 60000)
	// @Scheduled(fixedDelay = 60000)
	// @Scheduled(cron = "*/60 * * * * *")
	// @GetMapping("/retrieveStatusStock")
	// @ResponseBody
	// @Operation(summary = "Vérifier l'état des stocks",
	//            description = "Vérifie et affiche les produits dont la quantité est inférieure à la quantité minimale tolérée")
	// public void retrieveStatusStock() {
	//     stockService.retrieveStatusStock();
	// }
}