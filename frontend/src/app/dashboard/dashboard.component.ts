import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BusinessApiService, ENTITY_CONFIGS, EntityRecord } from '../business-api.service';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  stats = [
    { label: 'Produits', value: 0 },
    { label: 'Stocks faibles', value: 0 },
    { label: 'Fournisseurs', value: 0 },
    { label: 'Factures', value: 0 },
    { label: 'Reglements', value: 0 }
  ];
  lowStocks: EntityRecord[] = [];
  loading = true;

  constructor(private api: BusinessApiService) {}

  ngOnInit(): void {
    forkJoin({
      produits: this.safeList('produits'),
      stocks: this.safeList('stocks'),
      fournisseurs: this.safeList('fournisseurs'),
      factures: this.safeList('factures'),
      reglements: this.safeList('reglements')
    }).subscribe((data) => {
      this.lowStocks = data.stocks.filter((stock) => Number(stock['qte']) <= Number(stock['qteMin']));
      this.stats = [
        { label: 'Produits', value: data.produits.length },
        { label: 'Stocks faibles', value: this.lowStocks.length },
        { label: 'Fournisseurs', value: data.fournisseurs.length },
        { label: 'Factures', value: data.factures.length },
        { label: 'Reglements', value: data.reglements.length }
      ];
      this.loading = false;
    });
  }

  private safeList(key: keyof typeof ENTITY_CONFIGS) {
    return this.api.list(ENTITY_CONFIGS[key]).pipe(catchError(() => of([])));
  }
}
