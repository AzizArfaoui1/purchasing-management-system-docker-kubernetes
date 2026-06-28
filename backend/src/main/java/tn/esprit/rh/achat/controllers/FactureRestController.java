package tn.esprit.rh.achat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.services.IFactureService;

import java.util.Date;
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
@Tag(name = "Gestion des factures", description = "APIs pour la gestion des factures")
@RequestMapping("/facture")
@CrossOrigin("*")
public class FactureRestController {

    @Autowired
    IFactureService factureService;

    // http://localhost:8089/SpringMVC/facture/retrieve-all-factures
    @GetMapping("/retrieve-all-factures")
    @ResponseBody
    @Operation(summary = "Récupérer toutes les factures",
            description = "Retourne la liste de toutes les factures")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public List<Facture> getFactures() {
        List<Facture> list = factureService.retrieveAllFactures();
        return list;
    }

    // http://localhost:8089/SpringMVC/facture/retrieve-facture/8
    @GetMapping("/retrieve-facture/{facture-id}")
    @ResponseBody
    @Operation(summary = "Récupérer une facture par ID",
            description = "Retourne une facture spécifique basée sur son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture trouvée"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    public Facture retrieveFacture(
            @Parameter(description = "ID de la facture à récupérer", required = true)
            @PathVariable("facture-id") Long factureId) {
        return factureService.retrieveFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/facture/add-facture/{fournisseur-id}
    @PostMapping("/add-facture")
    @ResponseBody
    @Operation(summary = "Ajouter une nouvelle facture",
            description = "Crée une nouvelle facture dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture ajoutée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public Facture addFacture(
            @Parameter(description = "Objet facture à ajouter", required = true)
            @RequestBody Facture f) {
        Facture facture = factureService.addFacture(f);
        return facture;
    }

    /*
     * une facture peut etre annulé si elle a été saisie par erreur Pour ce
     * faire, il suffit de mettre le champs active à false
     */
    // http://localhost:8089/SpringMVC/facture/cancel-facture/{facture-id}
    @PutMapping("/cancel-facture/{facture-id}")
    @ResponseBody
    @Operation(summary = "Annuler une facture",
            description = "Annule une facture en mettant le champ active à false")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facture annulée avec succès"),
            @ApiResponse(responseCode = "404", description = "Facture non trouvée")
    })
    public void cancelFacture(
            @Parameter(description = "ID de la facture à annuler", required = true)
            @PathVariable("facture-id") Long factureId) {
        factureService.cancelFacture(factureId);
    }

    // http://localhost:8089/SpringMVC/facture/getFactureByFournisseur/{fournisseur-id}
    @GetMapping("/getFactureByFournisseur/{fournisseur-id}")
    @ResponseBody
    @Operation(summary = "Récupérer les factures d'un fournisseur",
            description = "Retourne la liste des factures associées à un fournisseur spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Fournisseur non trouvé")
    })
    public List<Facture> getFactureByFournisseur(
            @Parameter(description = "ID du fournisseur", required = true)
            @PathVariable("fournisseur-id") Long fournisseurId) {
        return factureService.getFacturesByFournisseur(fournisseurId);
    }

    // http://localhost:8089/SpringMVC/facture/assignOperateurToFacture/1/1
    @PutMapping(value = "/assignOperateurToFacture/{idOperateur}/{idFacture}")
    @Operation(summary = "Assigner un opérateur à une facture",
            description = "Associe un opérateur existant à une facture existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opérateur assigné avec succès"),
            @ApiResponse(responseCode = "404", description = "Opérateur ou facture non trouvé")
    })
    public void assignOperateurToFacture(
            @Parameter(description = "ID de l'opérateur", required = true)
            @PathVariable("idOperateur") Long idOperateur,
            @Parameter(description = "ID de la facture", required = true)
            @PathVariable("idFacture") Long idFacture) {
        factureService.assignOperateurToFacture(idOperateur, idFacture);
    }

    // http://localhost:8089/SpringMVC/facture/pourcentageRecouvrement/{startDate}/{endDate}
    @GetMapping(value = "/pourcentageRecouvrement/{startDate}/{endDate}")
    @Operation(summary = "Calculer le pourcentage de recouvrement",
            description = "Calcule le pourcentage de recouvrement entre deux dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pourcentage calculé avec succès"),
            @ApiResponse(responseCode = "400", description = "Dates invalides")
    })
    public float pourcentageRecouvrement(
            @Parameter(description = "Date de début (format ISO: yyyy-MM-dd)", required = true)
            @PathVariable(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "Date de fin (format ISO: yyyy-MM-dd)", required = true)
            @PathVariable(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        try {
            return factureService.pourcentageRecouvrement(startDate, endDate);
        } catch (Exception e) {
            return 0;
        }
    }
}