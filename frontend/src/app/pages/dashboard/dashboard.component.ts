import { CommonModule } from '@angular/common';
import { Component, ChangeDetectorRef } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { take } from 'rxjs';
import { environment } from '../../../environments/environment';

import { TokenService } from '../../core/services/token.service';

type DashboardSection = 'absences' | 'conges' | 'emplois';

type AbsenceResponseDto = {
  dateInterval?: string;
  libelle?: string;
  motif?: string;
  duree?: number;
};

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  active: DashboardSection = 'absences';
  error: string | null = null;
  data: AbsenceResponseDto[] = [];

  constructor(
    private readonly http: HttpClient,
    private readonly tokenService: TokenService,
    private readonly cd: ChangeDetectorRef
  ) {
    this.load('absences');
  }

  select(section: DashboardSection) {
    if (this.active === section) {
      this.load(section);
      return;
    }
    this.load(section);
  }

  refresh() {
    this.load(this.active);
  }

  private load(section: DashboardSection) {
    this.active = section;
    this.error = null;
    this.data = [];

    const userId = this.tokenService.getUserId();
    if (!userId) {
      this.error = 'Utilisateur non authentifié (ID manquant)';
      return;
    }

    let url = '';
    if (section === 'absences') {
      url = `${environment.apiUrl}/absences/me/${userId}`;
    } else if (section === 'conges') {
      url = `${environment.apiUrl}/conges/me/${userId}`;
    } else {
      url = `${environment.apiUrl}/emplois/me/${userId}`;
    }

    const req$ = this.http.get<unknown>(url);

    req$.pipe(take(1)).subscribe({
      next: (res) => {
        console.log('DATA RECEIVED:', res);
        console.log('REQUEST URL:', url, 'USER ID:', userId);

        let parsed: any = res;
        if (typeof res === 'string') {
          try {
            parsed = JSON.parse(res);
            console.log('Parsed string response to JSON:', parsed);
          } catch (err) {
            console.warn('Response is a string but not JSON:', res);
          }
        }

        // Accept plusieurs formats : tableau, { absences: [...] }, { data: [...] } ou objet indexé
        if (Array.isArray(parsed)) {
          this.data = parsed as AbsenceResponseDto[];
        } else if (parsed && typeof parsed === 'object') {
          if ('absences' in parsed && Array.isArray((parsed as any).absences)) {
            this.data = (parsed as any).absences;
          } else if ('data' in parsed && Array.isArray((parsed as any).data)) {
            this.data = (parsed as any).data;
          } else {
            const keys = Object.keys(parsed);
            if (keys.length > 0 && keys.every((k) => /^\d+$/.test(k))) {
              const arr = keys.sort((a, b) => +a - +b).map((k) => (parsed as any)[k]);
              this.data = arr as AbsenceResponseDto[];
            } else {
              this.data = [];
            }
          }
        } else {
          this.data = [];
        }

        console.log('DASHBOARD: data.length=', this.data.length);

        if (this.data.length === 0) {
          this.error = 'Aucune donnée reçue du serveur.';
        } else {
          this.error = null;
        }

        // Force an immediate change detection to ensure the template updates
        try {
          this.cd.detectChanges();
        } catch (err) {
          console.warn('Change detection failed:', err);
        }
      },
      error: (e: unknown) => {
        if (e instanceof HttpErrorResponse) {
          if (e.status === 0) {
            this.error = 'Impossible de joindre le serveur (Gateway).';
          } else {
            const msg =
              typeof e.error === 'string' ? e.error : (e.error?.message as string | undefined);
            this.error = msg ? `HTTP ${e.status}: ${msg}` : `HTTP ${e.status}`;
          }
        } else if (e instanceof Error) {
          this.error = e.message;
        } else {
          this.error = 'Erreur lors du chargement.';
        }

        // Ensure the template reflects the error
        try {
          this.cd.detectChanges();
        } catch (err) {
          console.warn('Change detection failed:', err);
        }
      }
    });
  }

  get absences(): AbsenceResponseDto[] {
    return this.active === 'absences' ? this.data : [];
  }

  get absencesTotal(): number {
    return this.absences.length;
  }

  get absencesTotalJours(): number {
    return this.absences.reduce((acc, a) => acc + (a.duree ?? 0), 0);
  }

  get absencesParType(): Array<{ libelle: string; count: number; jours: number }> {
    const map = new Map<string, { count: number; jours: number }>();
    for (const a of this.absences) {
      const lib = (a.libelle ?? 'INCONNU').trim() || 'INCONNU';
      const item = map.get(lib) || { count: 0, jours: 0 };
      item.count += 1;
      item.jours += a.duree ?? 0;
      map.set(lib, item);
    }
    return Array.from(map.entries())
      .map(([libelle, v]) => ({ libelle, count: v.count, jours: v.jours }))
      .sort((a, b) => b.count - a.count);
  }

  get absencesRepartitionJustification(): Array<{ label: string; count: number }> {
    let justifiees = 0;
    let nonJustifiees = 0;
    let sansJustifRequise = 0;

    for (const a of this.absences) {
      const motif = (a.motif ?? '').toString();
      if (motif === 'NONE') {
        sansJustifRequise += 1;
      } else if (motif.trim().length > 0) {
        justifiees += 1;
      } else {
        nonJustifiees += 1;
      }
    }

    return [
      { label: 'Justifiées', count: justifiees },
      { label: 'Non justifiées', count: nonJustifiees },
      { label: 'Sans justification requise', count: sansJustifRequise }
    ];
  }

  // Colonnes à afficher pour la vue brute (utilise les clés du premier objet)
  get rawColumns(): string[] {
    return this.data && this.data.length ? Object.keys(this.data[0]) : [];
  }

  formatCell(value: any): string {
    if (value === null || value === undefined) return '';
    if (typeof value === 'object') return JSON.stringify(value);
    return String(value);
  }

  getCell(row: any, col: string): any {
    return row ? row[col] : undefined;
  }

  get prochainesAbsences(): AbsenceResponseDto[] {
    const now = new Date();
    const parseStart = (dateInterval?: string): Date | null => {
      if (!dateInterval) return null;
      const first = dateInterval.split(' à ')[0]?.trim();
      if (!first) return null;
      const d = new Date(first);
      return isNaN(d.getTime()) ? null : d;
    };

    return this.absences
      .map((a) => ({ a, d: parseStart(a.dateInterval) }))
      .filter((x): x is { a: AbsenceResponseDto; d: Date } => !!x.d)
      .filter((x) => x.d.getTime() >= new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime())
      .sort((x, y) => x.d.getTime() - y.d.getTime())
      .slice(0, 3)
      .map((x) => x.a);
  }
}
