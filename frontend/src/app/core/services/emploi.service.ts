import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, take } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

export interface CreateEmploiDTO {
  date: string;
  heure: string;
  tache: string;
  taches?: string[];
}

export interface RHEmploiDTO {
  id?: string;
  date?: string;
  heure?: string;
  tache?: string;
  taches?: string[];
  employeId?: string;
}

@Injectable({
  providedIn: 'root'
})
export class EmploiService {
  private readonly baseUrl = environment.apiUrl;
  private jsonHeaders = { headers: new HttpHeaders({ 'Content-Type': 'application/json' }) };
  private canFetchEmployees = !!((environment as any).employesEndpointAvailable);

  constructor(private readonly http: HttpClient) {}

  getEmployesByRh(rhId: string): Observable<Array<{ id: string; name?: string }>> {
    if (!this.canFetchEmployees) {
      console.warn('Employes endpoint disabled by configuration; returning empty list');
      return of([]);
    }

    const url = `${this.baseUrl}/rh/${encodeURIComponent(rhId)}/employes`;
    return this.http.get<any[]>(url).pipe(
      take(1),
      map((arr) => arr.map((p: any) => ({ id: p.id, name: p.name || p.nom || p.username || p.email }))),
      catchError((err) => {
        console.warn('Failed to fetch employees', err);
        return of([]);
      })
    );
  }

  getEmploisByRh(rhId: string): Observable<RHEmploiDTO[]> {
    const url = `${this.baseUrl}/emplois/rh/${encodeURIComponent(rhId)}`;
    return this.http.get<RHEmploiDTO[]>(url).pipe(take(1), catchError(this.handleError));
  }

  createEmploi(rhId: string, employeId: string, dto: CreateEmploiDTO): Observable<RHEmploiDTO> {
    const url = `${this.baseUrl}/emplois/rh/${encodeURIComponent(rhId)}/employe/${encodeURIComponent(employeId)}/emploi`;
    return this.http.post<RHEmploiDTO>(url, dto, this.jsonHeaders).pipe(take(1), catchError(this.handleError));
  }

  // Endpoint changed: DELETE /emplois/{emploiId}
  deleteEmploi(emploiId: string): Observable<HttpResponse<void>> {
    const url = `${this.baseUrl}/emplois/${encodeURIComponent(emploiId)}`;
    return this.http.delete<void>(url, { observe: 'response' as const }).pipe(
      take(1),
      catchError(this.handleError)
    );
  }

  private handleError = (err: HttpErrorResponse) => {
    let msg = 'Erreur serveur';
    if (err.status === 400) msg = 'Données invalides (400)';
    else if (err.status === 403) msg = 'Accès refusé (403)';
    else if (err.status === 404) msg = 'Ressource introuvable (404)';
    else if (err.status === 405) msg = 'Méthode non autorisée (405)';
    else if (err.status === 429) msg = 'Trop de requêtes (429)';
    else if (err.status === 0) msg = 'Impossible de joindre le serveur';
    return throwError(() => new Error(msg));
  };
}
