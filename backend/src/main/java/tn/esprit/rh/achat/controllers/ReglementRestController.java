package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Reglement;
import tn.esprit.rh.achat.services.IReglementService;

import java.util.Date;
import java.util.List;

// SpringDoc OpenAPI 3 imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@Tag(name = "Gestion des règlements", description = "APIs pour la gestion des règlements")
@RequestMapping("/reglement")
@CrossOrigin("*")
public class ReglementRestController {

    @Autowired
    IReglementService reglementService;

    // http://localhost:8089/SpringMVC/reglement/add-reglement
    @PostMapping("/add-reglement")
    @ResponseBody
    @Operation(summary = "Ajouter un nouveau règlement",
            description = "Crée un nouveau règlement dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Règlement ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public Reglement addReglement(
            @Parameter(description = "Objet règlement à ajouter", required = true)
            @RequestBody Reglement r) {
        Reglement reglement = reglementService.addReglement(r);
        return reglement;
    }

    // http://localhost:8089/SpringMVC/reglement/retrieve-all-reglements
    @GetMapping("/retrieve-all-reglements")
    @ResponseBody
    @Operation(summary = "Récupérer tous les règlements",
            description = "Retourne la liste de tous les règlements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<Reglement> getReglement() {
        List<Reglement> list = reglementService.retrieveAllReglements();
        return list;
    }

    // http://localhost:8089/SpringMVC/reglement/retrieve-reglement/8
    @GetMapping("/retrieve-reglement/{reglement-id}")
    @ResponseBody
    @Operation(summary = "Récupérer un règlement par ID",
            description = "Retourne un règlement spécifique basé sur son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Règlement trouvé"),
            @ApiResponse(responseCode = "404", description = "Règlement non trouvé")
    })
    public Reglement retrieveReglement(
            @Parameter(description = "ID du règlement à récupérer", required = true)
            @PathVariable("reglement-id") Long reglementId) {
        return reglementService.retrieveReglement(reglementId);
    }

    // http://localhost:8089/SpringMVC/reglement/retrieveReglementByFacture/8
    @GetMapping("/retrieveReglementByFacture/{facture-id}")
    @ResponseBody
    @Operation(summary = "Récupérer les règlements d'une facture",
            description = "Retourne la liste des règlements associés à une facture spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    public List<Reglement> retrieveReglementByFacture(
            @Parameter(description = "ID de la facture", required = true)
            @PathVariable("facture-id") Long factureId) {
        return reglementService.retrieveReglementByFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/reglement/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate}
    @GetMapping(value = "/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate}")
    @Operation(summary = "Calculer le chiffre d'affaires entre deux dates",
            description = "Retourne le montant total des règlements entre deux dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chiffre d'affaires calculé avec succès"),
            @ApiResponse(responseCode = "400", description = "Dates invalides")
    })
    public float getChiffreAffaireEntreDeuxDate(
            @Parameter(description = "Date de début (format ISO: yyyy-MM-dd)", required = true)
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "Date de fin (format ISO: yyyy-MM-dd)", required = true)
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            return reglementService.getChiffreAffaireEntreDeuxDate(startDate, endDate);
        } catch (Exception e) {
            return 0;
        }
    }
}