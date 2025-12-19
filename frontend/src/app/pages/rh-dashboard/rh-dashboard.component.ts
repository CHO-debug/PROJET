import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { take } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

import { TokenService } from '../../core/services/token.service';
import { EmploiService, CreateEmploiDTO, RHEmploiDTO } from '../../core/services/emploi.service';

type EmploiDto = {
  id?: string;
  titre?: string;
  libelle?: string;
  date?: string;
  jour?: string;
  heure?: string;
  time?: string;
  debut?: string;
  fin?: string;
  start?: string;
  end?: string;
  tache?: string;
  taches?: string[];
  employeId?: string;
  employe?: string;
  employeeId?: string;
  name?: string;
};

// Move ActiveSection type to top-level (cannot declare `type` inside a class)
export type ActiveSection = 'absences' | 'conges' | 'emplois' | 'calendrier';

@Component({
  selector: 'app-rh-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './rh-dashboard.component.html',
  styleUrl: './rh-dashboard.component.css'
})
export class RhDashboardComponent implements OnInit {
  emplois: EmploiDto[] = [];
  loading = false;
  error: string | null = null;

  // Optionally fetched employees for selection in the create form
  employees: Array<{ id: string; name?: string }> = [];

  // Cache/guard to avoid repeated GET calls
  private lastLoadAt: number | null = null;
  private readonly loadCacheMs = 30_000; // cache for 30 seconds

  // Layout state to mimic employee dashboard styles
  theme: 'light' | 'dark' = 'light';
  sidebarOpen = false;

  active: ActiveSection = 'emplois';

  // Create form
  createForm!: FormGroup;
  showCreateForm = false;
  successMessage: string | null = null;

  get pageTitle(): string {
    if (this.active === 'absences') return 'Mes absences';
    if (this.active === 'conges') return 'Mes congés';
    if (this.active === 'emplois') return 'Emplois créés';
    return 'Calendrier';
  }

  select(section: ActiveSection) {
    // No-op if user clicks the active section again (prevents repeated loads)
    if (this.active === section) {
      return;
    }

    this.active = section;

    if (section === 'emplois') {
      this.loadEmplois();
    } else {
      // For now, clear emplois when switching away from emplois
      this.emplois = [];
      this.error = null;
    }

    this.closeSidebar();
  }

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private http: HttpClient,
    private readonly cd: ChangeDetectorRef,
    private readonly fb: FormBuilder,
    private readonly emploiService: EmploiService
  ) {
    const role = this.tokenService.getRole();
    const isRh = role ? role.toLowerCase().includes('rh') : false;
    if (!isRh) {
      // Si l'utilisateur n'est pas RH, rediriger vers le dashboard standard
      this.router.navigateByUrl('/dashboard');
    }
  }

  toggleTheme(): void {
    this.theme = this.theme === 'dark' ? 'light' : 'dark';
  }

  toggleSidebar(): void {
    this.sidebarOpen = !this.sidebarOpen;
  }

  closeSidebar(): void {
    this.sidebarOpen = false;
  }

  ngOnInit(): void {
    // Force initial load once on init
    this.loadEmplois(true);
    this.fetchEmployees();

    this.createForm = this.fb.group({
      date: ['', Validators.required],
      heure: ['', Validators.required],
      tache: ['', Validators.required],
      tachesText: [''],
      employeId: ['', Validators.required]
    });
  }

  // Try to fetch a list of employees for the RH to select from.
  // NOTE: do not call endpoints that are known to return 403/404 in this backend.
  // The service will return an empty list if the feature is not available.
  private fetchEmployees(): void {
    const rhId = this.tokenService.getUserId();
    if (!rhId) return;

    this.emploiService.getEmployesByRh(rhId).pipe(take(1)).subscribe({
      next: (list) => {
        this.employees = list || [];
      },
      error: (err) => {
        console.warn('fetchEmployees failed', err);
        this.employees = [];
      }
    });
  }

  loadEmplois(force = false): void {
    const rhId = this.tokenService.getUserId();
    if (!rhId) {
      this.error = 'ID RH manquant';
      return;
    }

    // Avoid repeated calls: if already loading or recently loaded and not forced, skip
    if (!force) {
      if (this.loading) {
        console.log('RH: loadEmplois skipped because a load is already in progress');
        return;
      }
      if (this.lastLoadAt && (Date.now() - this.lastLoadAt) < this.loadCacheMs) {
        console.log('RH: loadEmplis skipped (cached within TTL)');
        return;
      }
    }

    this.loading = true;
    this.error = null;

    // Primary backend exposes GET /emplois/rh/{rhId}
    const urlPrimary = `${environment.apiUrl}/emplois/rh/${rhId}`;
    const urlFallback = `${environment.apiUrl}/rh/${rhId}`; // legacy fallback

    console.log('RH: requesting emplois from', urlPrimary);
    this.emploiService.getEmploisByRh(rhId).pipe(take(1)).subscribe({
      next: (res) => {
        // Service returns an array of RHEmploiDTO -> pass to normalization handler
        this.handleEmploisResponse(res || []);
      },
      error: (e: unknown) => {
        console.error('RH: error fetching emplois via service', e);
        if (e instanceof Error && e.message?.includes('404')) {
          // as a last resort try legacy endpoint
          console.log('RH: service reported 404, trying legacy fallback', urlFallback);
          this.http.get<unknown>(urlFallback).pipe(take(1)).subscribe({
            next: (r) => this.handleEmploisResponse(r),
            error: (err) => this.handleError(err)
          });
        } else {
          this.handleError(e);
        }
      }
    });
  }

  private handleEmploisResponse(res: unknown): void {
    let parsed: any = res;
    if (typeof res === 'string') {
      try {
        parsed = JSON.parse(res);
      } catch (err) {
        parsed = res;
      }
    }

    if (Array.isArray(parsed)) {
      this.emplois = parsed as EmploiDto[];
    } else if (parsed && typeof parsed === 'object') {
      if (Array.isArray(parsed.emplois)) {
        this.emplois = parsed.emplois as EmploiDto[];
      } else if (Array.isArray(parsed.data)) {
        this.emplois = parsed.data as EmploiDto[];
      } else {
        const keys = Object.keys(parsed || {});
        if (keys.length > 0 && keys.every((k) => /^\d+$/.test(k))) {
          const arr = keys.sort((a, b) => +a - +b).map((k) => (parsed as any)[k]);
          this.emplois = arr as EmploiDto[];
        } else {
          this.emplois = [];
        }
      }
    } else {
      this.emplois = [];
    }

    // Normalize each emploi: ensure taches is an array and canonical keys exist
    this.emplois = this.emplois.map((it: any) => ({
      ...it,
      taches: Array.isArray(it?.taches) ? it.taches : [],
      date: it?.date ?? it?.jour ?? '',
      heure: it?.heure ?? it?.time ?? '',
      tache: it?.tache ?? it?.titre ?? it?.libelle ?? '',
      employeId: it?.employeId ?? it?.employe ?? it?.employeeId ?? ''
    }));

    // Debug + ensure view updates
    console.log('RH: emplois loaded', this.emplois.length, this.emplois);
    this.loading = false;
    this.lastLoadAt = Date.now();
    try {
      this.cd.detectChanges();
    } catch (err) {
      console.warn('RH: change detection failed', err);
    }
  }

  private handleError(e: unknown): void {
    if (e instanceof HttpErrorResponse) {
      if (e.status === 0) {
        this.error = 'Impossible de joindre le serveur (Gateway).';
      } else {
        const msg = typeof e.error === 'string' ? e.error : (e.error?.message as string | undefined);
        this.error = msg ? `HTTP ${e.status}: ${msg}` : `HTTP ${e.status}`;
      }
    } else if (e instanceof Error) {
      this.error = e.message;
    } else {
      this.error = 'Erreur lors du chargement des emplois.';
    }

    this.loading = false;
    // Ensure template updates and show the error
    console.warn('RH: emplois load error', e);
    try {
      this.cd.detectChanges();
    } catch (err) {
      console.warn('RH: change detection failed on error', err);
    }
  }

  // ---------- CREATE EMPLOI ----------
  toggleCreateForm(): void {
    this.showCreateForm = !this.showCreateForm;
    this.successMessage = null;
    if (!this.showCreateForm) {
      this.createForm.reset();
    }
  }

  submitCreateForm(): void {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    const rhId = this.tokenService.getUserId();
    if (!rhId) {
      this.error = 'ID RH manquant';
      return;
    }

    const payload: any = {
      // Build a JSON payload that matches the backend CreateEmploiDTO
      date: this.createForm.value.date,
      heure: this.createForm.value.heure,
      tache: this.createForm.value.tache,
      taches: (this.createForm.value.tachesText || '')
        .split(/\r?\n/)
        .map((s: string) => s.trim())
        .filter((s: string) => s.length > 0),
      // For endpoints that accept employeId in the path, we may omit it in the body
      employeId: this.createForm.value.employeId || undefined
    };

    // Build candidate endpoints (try the specific new contract first if employeId is provided)
    // Enforce that employeId is provided — backend requires it in the path
    const employeId = this.createForm.value.employeId?.toString().trim();
    if (!employeId) {
      this.error = 'Identifiant de l\'employé requis pour créer l\'emploi.';
      this.createForm.get('employeId')?.markAsTouched();
      return;
    }

    // Build the DTO body (do not include employeId in the body since it's in the path)
    const body: CreateEmploiDTO = {
      date: payload.date,
      heure: payload.heure,
      tache: payload.tache,
      taches: payload.taches
    };

    // Local success handler used after POST
    const doSuccess = () => {
      this.successMessage = 'Emploi créé avec succès.';
      this.showCreateForm = false;
      this.createForm.reset();
      // Force reload after create to get server-side representation
      this.loadEmplois(true);
    };

    this.error = null;
    this.loading = true;
    console.log('RH: creating emploi via service', { rhId, employeId, body });

    this.emploiService.createEmploi(rhId, employeId, body).pipe(take(1)).subscribe({
      next: (created) => {
        this.loading = false;
        doSuccess();
      },
      error: (err: unknown) => {
        console.error('RH: createEmploi failed', err);
        this.loading = false;
        if (err instanceof Error) this.error = err.message; else this.error = 'Erreur lors de la création de l\'emploi.';
      }
    });
  }

  logout(): void {
    this.tokenService.clearAll();
    this.router.navigateByUrl('/login');
  }

  // Delete flow replaced with modal confirmation (do not use window.confirm)
  pendingDelete: EmploiDto | null = null;
  showDeleteModal = false;

  promptDelete(emploi: EmploiDto): void {
    console.log('promptDelete called with', emploi);
    this.pendingDelete = emploi;
    this.showDeleteModal = true;
    this.error = null;
    this.successMessage = null;
  }

  cancelDelete(): void {
    this.pendingDelete = null;
    this.showDeleteModal = false;
  }

  confirmDelete(): void {
    console.log('confirmDelete, pendingDelete=', this.pendingDelete);

    if (!this.pendingDelete) {
      this.error = 'Aucun emploi sélectionné.';
      this.showDeleteModal = false;
      return;
    }

    const asAny: any = this.pendingDelete as any;
    let emploiId: string | undefined = asAny.id ?? asAny._id ?? asAny.emploiId ?? undefined;

    // If still not found, try to locate a 24-char hex string in the values (common Mongo id)
    if (!emploiId) {
      for (const key of Object.keys(asAny)) {
        const val = asAny[key];
        if (typeof val === 'string' && /^[0-9a-fA-F]{24}$/.test(val)) {
          emploiId = val;
          break;
        }
      }
    }

    if (!emploiId) {
      console.warn('Could not determine emploiId from pendingDelete', this.pendingDelete);
      this.error = 'Identifiant de l\'emploi manquant.';
      this.showDeleteModal = false;
      this.pendingDelete = null;
      return;
    }

    this.loading = true;
    this.error = null;

    console.log('Deleting emploiId:', emploiId);
    this.emploiService.deleteEmploi(emploiId).pipe(take(1)).subscribe({
      next: (resp) => {
        // Close popup when server returns 204 No Content
        if (resp && resp.status === 204) {
          this.successMessage = 'Emploi supprimé.';
          this.emplois = this.emplois.filter((e) => {
            const id = (e as any).id ?? (e as any)._id ?? (e as any).emploiId ?? undefined;
            return id !== emploiId;
          });
          this.lastLoadAt = Date.now();
          this.pendingDelete = null;
          this.showDeleteModal = false;
        } else {
          // Other success statuses are tolerated but logged; still close the modal
          console.warn('RH: deleteEmploi returned status', resp?.status);
          this.successMessage = 'Emploi supprimé.';
          this.emplois = this.emplois.filter((e) => {
            const id = (e as any).id ?? (e as any)._id ?? (e as any).emploiId ?? undefined;
            return id !== emploiId;
          });
          this.lastLoadAt = Date.now();
          this.pendingDelete = null;
          this.showDeleteModal = false;
        }
        this.loading = false;
      },
      error: (e: unknown) => {
        console.error('RH: deleteEmploi failed', e);
        this.loading = false;
        this.pendingDelete = null;
        this.showDeleteModal = false;
        if (e instanceof Error) this.error = e.message; else this.error = 'Erreur lors de la suppression.';
      }
    });
  }
}
