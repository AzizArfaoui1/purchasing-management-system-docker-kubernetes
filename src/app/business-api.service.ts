import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export type EntityKey = 'produits' | 'stocks' | 'categories' | 'fournisseurs' | 'secteurs' | 'operateurs' | 'factures' | 'reglements';

export interface EntityField {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'email' | 'select' | 'boolean';
  required?: boolean;
  min?: number;
  options?: string[];
  hiddenOnList?: boolean;
  readonly?: boolean;
}

export interface EntityConfig {
  key: EntityKey;
  title: string;
  idKey: string;
  endpoints: {
    list: string;
    get?: string;
    create?: string;
    update?: string;
    delete?: string;
  };
  fields: EntityField[];
  searchable: string[];
  readonly?: boolean;
}

export type EntityRecord = Record<string, unknown>;

export const API_BASE_URL = 'http://localhost:8089/SpringMVC';

export const ENTITY_CONFIGS: Record<EntityKey, EntityConfig> = {
  produits: {
    key: 'produits',
    title: 'Produits',
    idKey: 'idProduit',
    endpoints: {
      list: '/produit/retrieve-all-produits',
      get: '/produit/retrieve-produit',
      create: '/produit/add-produit',
      update: '/produit/modify-produit',
      delete: '/produit/remove-produit'
    },
    fields: [
      { key: 'codeProduit', label: 'Code', type: 'text', required: true },
      { key: 'libelleProduit', label: 'Libelle', type: 'text', required: true },
      { key: 'prix', label: 'Prix', type: 'number', required: true, min: 0 }
    ],
    searchable: ['codeProduit', 'libelleProduit']
  },
  stocks: {
    key: 'stocks',
    title: 'Stocks',
    idKey: 'idStock',
    endpoints: {
      list: '/stock/retrieve-all-stocks',
      get: '/stock/retrieve-stock',
      create: '/stock/add-stock',
      update: '/stock/modify-stock',
      delete: '/stock/remove-stock'
    },
    fields: [
      { key: 'libelleStock', label: 'Libelle', type: 'text', required: true },
      { key: 'qte', label: 'Quantite', type: 'number', required: true, min: 0 },
      { key: 'qteMin', label: 'Quantite min', type: 'number', required: true, min: 0 }
    ],
    searchable: ['libelleStock']
  },
  categories: {
    key: 'categories',
    title: 'Categories',
    idKey: 'idCategorieProduit',
    endpoints: {
      list: '/categorieProduit/retrieve-all-categorieProduit',
      get: '/categorieProduit/retrieve-categorieProduit',
      create: '/categorieProduit/add-categorieProduit',
      update: '/categorieProduit/modify-categorieProduit',
      delete: '/categorieProduit/remove-categorieProduit'
    },
    fields: [
      { key: 'codeCategorie', label: 'Code', type: 'text', required: true },
      { key: 'libelleCategorie', label: 'Libelle', type: 'text', required: true }
    ],
    searchable: ['codeCategorie', 'libelleCategorie']
  },
  fournisseurs: {
    key: 'fournisseurs',
    title: 'Fournisseurs',
    idKey: 'idFournisseur',
    endpoints: {
      list: '/fournisseur/retrieve-all-fournisseurs',
      get: '/fournisseur/retrieve-fournisseur',
      create: '/fournisseur/add-fournisseur',
      update: '/fournisseur/modify-fournisseur',
      delete: '/fournisseur/remove-fournisseur'
    },
    fields: [
      { key: 'nom', label: 'Nom', type: 'text' },
      { key: 'prenom', label: 'Prenom', type: 'text' },
      { key: 'code', label: 'Code', type: 'text', required: true },
      { key: 'libelle', label: 'Libelle', type: 'text', required: true },
      { key: 'categorieFournisseur', label: 'Categorie', type: 'select', required: true, options: ['ORDINAIRE', 'CONVENTIONNE'] },
      { key: 'detailFournisseur.email', label: 'Email', type: 'email', required: true },
      { key: 'detailFournisseur.adresse', label: 'Adresse', type: 'text', required: true },
      { key: 'detailFournisseur.matricule', label: 'Matricule', type: 'text', required: true },
      { key: 'detailFournisseur.dateDebutCollaboration', label: 'Debut collaboration', type: 'date' }
    ],
    searchable: ['nom', 'prenom', 'code', 'libelle']
  },
  secteurs: {
    key: 'secteurs',
    title: 'Secteurs',
    idKey: 'idSecteurActivite',
    endpoints: {
      list: '/secteurActivite/retrieve-all-secteurActivite',
      get: '/secteurActivite/retrieve-secteurActivite',
      create: '/secteurActivite/add-secteurActivite',
      update: '/secteurActivite/modify-secteurActivite',
      delete: '/secteurActivite/remove-secteurActivite'
    },
    fields: [
      { key: 'codeSecteurActivite', label: 'Code', type: 'text', required: true },
      { key: 'libelleSecteurActivite', label: 'Libelle', type: 'text', required: true }
    ],
    searchable: ['codeSecteurActivite', 'libelleSecteurActivite']
  },
  operateurs: {
    key: 'operateurs',
    title: 'Operateurs',
    idKey: 'idOperateur',
    endpoints: {
      list: '/operateur/retrieve-all-operateurs',
      get: '/operateur/retrieve-operateur',
      create: '/operateur/add-operateur',
      update: '/operateur/modify-operateur',
      delete: '/operateur/remove-operateur'
    },
    fields: [
      { key: 'nom', label: 'Nom', type: 'text', required: true },
      { key: 'prenom', label: 'Prenom', type: 'text', required: true },
      { key: 'password', label: 'Password', type: 'text', hiddenOnList: true }
    ],
    searchable: ['nom', 'prenom']
  },
  factures: {
    key: 'factures',
    title: 'Factures',
    idKey: 'idFacture',
    endpoints: {
      list: '/facture/retrieve-all-factures',
      get: '/facture/retrieve-facture',
      create: '/facture/add-facture'
    },
    fields: [
      { key: 'montantRemise', label: 'Remise', type: 'number', required: true, min: 0 },
      { key: 'montantFacture', label: 'Montant', type: 'number', required: true, min: 0 },
      { key: 'archivee', label: 'Archivee', type: 'boolean', readonly: true }
    ],
    searchable: ['idFacture', 'montantFacture']
  },
  reglements: {
    key: 'reglements',
    title: 'Reglements',
    idKey: 'idReglement',
    endpoints: {
      list: '/reglement/retrieve-all-reglements',
      get: '/reglement/retrieve-reglement',
      create: '/reglement/add-reglement'
    },
    fields: [
      { key: 'montantPaye', label: 'Montant paye', type: 'number', required: true, min: 0 },
      { key: 'montantRestant', label: 'Montant restant', type: 'number', required: true, min: 0 },
      { key: 'payee', label: 'Payee', type: 'boolean', required: true },
      { key: 'dateReglement', label: 'Date', type: 'date' }
    ],
    searchable: ['idReglement', 'montantPaye']
  }
};

@Injectable({ providedIn: 'root' })
export class BusinessApiService {
  readonly baseUrl = API_BASE_URL;

  constructor(private http: HttpClient) {}

  list(config: EntityConfig): Observable<EntityRecord[]> {
    return this.http.get<EntityRecord[]>(this.url(config.endpoints.list));
  }

  create(config: EntityConfig, payload: EntityRecord): Observable<EntityRecord> {
    return this.http.post<EntityRecord>(this.url(config.endpoints.create!), payload);
  }

  update(config: EntityConfig, payload: EntityRecord): Observable<EntityRecord> {
    return this.http.put<EntityRecord>(this.url(config.endpoints.update!), payload);
  }

  remove(config: EntityConfig, id: unknown): Observable<unknown> {
    return this.http.delete<unknown>(this.url(`${config.endpoints.delete}/${id}`));
  }

  assignProduitToStock(idProduit: unknown, idStock: unknown): Observable<unknown> {
    return this.http.put<unknown>(this.url(`/produit/assignProduitToStock/${idProduit}/${idStock}`), {});
  }

  assignSecteurToFournisseur(idSecteur: unknown, idFournisseur: unknown): Observable<unknown> {
    return this.http.put<unknown>(this.url(`/fournisseur/assignSecteurActiviteToFournisseur/${idSecteur}/${idFournisseur}`), {});
  }

  cancelFacture(idFacture: unknown): Observable<unknown> {
    return this.http.put<unknown>(this.url(`/facture/cancel-facture/${idFacture}`), {});
  }

  assignOperateurToFacture(idOperateur: unknown, idFacture: unknown): Observable<unknown> {
    return this.http.put<unknown>(this.url(`/facture/assignOperateurToFacture/${idOperateur}/${idFacture}`), {});
  }

  facturesByFournisseur(idFournisseur: unknown): Observable<EntityRecord[]> {
    return this.http.get<EntityRecord[]>(this.url(`/facture/getFactureByFournisseur/${idFournisseur}`));
  }

  reglementsByFacture(idFacture: unknown): Observable<EntityRecord[]> {
    return this.http.get<EntityRecord[]>(this.url(`/reglement/retrieveReglementByFacture/${idFacture}`));
  }

  chiffreAffaire(startDate: string, endDate: string): Observable<number> {
    return this.http.get<number>(this.url(`/reglement/getChiffreAffaireEntreDeuxDate/${startDate}/${endDate}`));
  }

  pourcentageRecouvrement(startDate: string, endDate: string): Observable<number> {
    return this.http.get<number>(this.url(`/facture/pourcentageRecouvrement/${startDate}/${endDate}`));
  }

  private url(path: string): string {
    return `${this.baseUrl}${path}`;
  }
}
