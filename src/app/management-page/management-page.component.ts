import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs';
import { BusinessApiService, EntityConfig, ENTITY_CONFIGS, EntityField, EntityKey, EntityRecord } from '../business-api.service';

@Component({
  selector: 'app-management-page',
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './management-page.component.html',
  styleUrl: './management-page.component.css'
})
export class ManagementPageComponent implements OnInit {
  config: EntityConfig = ENTITY_CONFIGS.produits;
  rows: EntityRecord[] = [];
  form: FormGroup = new FormGroup({});
  query = '';
  editing: EntityRecord | null = null;
  selected: EntityRecord | null = null;
  loading = false;
  saving = false;
  message = '';
  error = '';
  assignTarget = '';
  filterId = '';
  startDate = '';
  endDate = '';
  analyticsResult: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private api: BusinessApiService
  ) {}

  get columns(): EntityField[] {
    return this.config.fields.filter((field) => !field.hiddenOnList);
  }

  get filteredRows(): EntityRecord[] {
    const normalized = this.query.trim().toLowerCase();

    if (!normalized) {
      return this.rows;
    }

    return this.rows.filter((row) =>
      this.config.searchable.some((key) => String(this.valueOf(row, key) ?? '').toLowerCase().includes(normalized))
    );
  }

  get safeSelected(): EntityRecord | null {
    return this.selected ? this.withoutSensitiveFields(this.selected) : null;
  }

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.config = ENTITY_CONFIGS[data['entity'] as EntityKey];
      this.resetForm();
      this.load();
    });
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.api.list(this.config)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (rows) => this.rows = rows ?? [],
        error: () => this.error = 'Chargement impossible.'
      });
  }

  newItem(): void {
    this.editing = null;
    this.selected = null;
    this.resetForm();
  }

  edit(row: EntityRecord): void {
    this.editing = row;
    this.selected = row;
    this.resetForm(row);
  }

  view(row: EntityRecord): void {
    this.selected = row;
    this.editing = null;
  }

  save(): void {
    this.message = '';
    this.error = '';

    if (this.form.invalid || !this.validateBusinessRules()) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = this.buildPayload();
    const request = this.editing && this.config.endpoints.update
      ? this.api.update(this.config, { ...payload, [this.config.idKey]: this.editing[this.config.idKey] })
      : this.api.create(this.config, payload);

    this.saving = true;
    request.pipe(finalize(() => this.saving = false)).subscribe({
      next: () => {
        this.message = 'Enregistrement effectue.';
        this.newItem();
        this.load();
      },
      error: () => this.error = 'Enregistrement impossible.'
    });
  }

  remove(row: EntityRecord): void {
    const id = row[this.config.idKey];

    if (!id || !confirm(`Supprimer ${this.config.title} #${id} ?`)) {
      return;
    }

    this.api.remove(this.config, id).subscribe({
      next: () => {
        this.message = 'Suppression effectuee.';
        this.load();
      },
      error: () => this.error = 'Suppression impossible.'
    });
  }

  runAssignment(row: EntityRecord): void {
    this.message = '';
    this.error = '';

    if (!this.assignTarget) {
      this.error = 'Selectionnez un identifiant cible.';
      return;
    }

    const request = this.config.key === 'produits'
      ? this.api.assignProduitToStock(row[this.config.idKey], this.assignTarget)
      : this.config.key === 'fournisseurs'
        ? this.api.assignSecteurToFournisseur(this.assignTarget, row[this.config.idKey])
        : this.api.assignOperateurToFacture(this.assignTarget, row[this.config.idKey]);

    request.subscribe({
      next: () => {
        this.message = 'Assignation effectuee.';
        this.assignTarget = '';
      },
      error: () => this.error = 'Assignation impossible.'
    });
  }

  cancelFacture(row: EntityRecord): void {
    this.api.cancelFacture(row[this.config.idKey]).subscribe({
      next: () => {
        this.message = 'Facture annulee.';
        this.load();
      },
      error: () => this.error = 'Annulation impossible.'
    });
  }

  filterSpecial(): void {
    if (!this.filterId) {
      this.load();
      return;
    }

    const request = this.config.key === 'factures'
      ? this.api.facturesByFournisseur(this.filterId)
      : this.api.reglementsByFacture(this.filterId);

    request.subscribe({
      next: (rows) => this.rows = rows ?? [],
      error: () => this.error = 'Filtrage impossible.'
    });
  }

  runAnalytics(): void {
    if (!this.startDate || !this.endDate) {
      this.error = 'Choisissez deux dates.';
      return;
    }

    const request = this.config.key === 'reglements'
      ? this.api.chiffreAffaire(this.startDate, this.endDate)
      : this.api.pourcentageRecouvrement(this.startDate, this.endDate);

    request.subscribe({
      next: (value) => this.analyticsResult = value,
      error: () => this.error = 'Calcul impossible.'
    });
  }

  valueOf(row: EntityRecord, key: string): unknown {
    return key.split('.').reduce<unknown>((value, part) => {
      if (value && typeof value === 'object') {
        return (value as EntityRecord)[part];
      }
      return undefined;
    }, row);
  }

  controlKey(field: EntityField): string {
    return field.key.replace(/\./g, '__');
  }

  private resetForm(row?: EntityRecord): void {
    const controls: Record<string, FormControl> = {};

    for (const field of this.config.fields.filter((item) => !item.readonly)) {
      controls[this.controlKey(field)] = new FormControl(this.valueOf(row ?? {}, field.key) ?? this.defaultValue(field), this.validatorsFor(field));
    }

    this.form = this.formBuilder.group(controls);
  }

  private validatorsFor(field: EntityField) {
    const validators = [];

    if (field.required) {
      validators.push(Validators.required);
    }

    if (field.type === 'email') {
      validators.push(Validators.email);
    }

    if (typeof field.min === 'number') {
      validators.push(Validators.min(field.min));
    }

    return validators;
  }

  private defaultValue(field: EntityField): unknown {
    return field.type === 'boolean' ? false : '';
  }

  private buildPayload(): EntityRecord {
    const payload: EntityRecord = {};

    for (const field of this.config.fields.filter((item) => !item.readonly)) {
      this.assignNested(payload, field.key, this.form.value[this.controlKey(field)]);
    }

    return payload;
  }

  private assignNested(target: EntityRecord, key: string, value: unknown): void {
    const parts = key.split('.');
    let cursor = target;

    parts.slice(0, -1).forEach((part) => {
      cursor[part] = cursor[part] ?? {};
      cursor = cursor[part] as EntityRecord;
    });

    cursor[parts[parts.length - 1]] = value;
  }

  private validateBusinessRules(): boolean {
    if (this.config.key === 'stocks') {
      const qte = Number(this.form.value.qte);
      const qteMin = Number(this.form.value.qteMin);

      if (qteMin > qte) {
        this.error = 'La quantite minimale doit etre inferieure ou egale a la quantite.';
        return false;
      }
    }

    return true;
  }

  private withoutSensitiveFields(value: EntityRecord): EntityRecord {
    return Object.entries(value).reduce<EntityRecord>((result, [key, item]) => {
      if (key.toLowerCase().includes('password')) {
        return result;
      }

      if (item && typeof item === 'object' && !Array.isArray(item)) {
        result[key] = this.withoutSensitiveFields(item as EntityRecord);
        return result;
      }

      result[key] = item;
      return result;
    }, {});
  }
}
