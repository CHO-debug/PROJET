import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface LoginRequest {
  email: string;
  motDePasse: string;
}

export interface LoginResponse {
  token?: string;
  id:string;
  nom:string;
  email:string
  role: string,
  [key: string]: unknown;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private readonly http: HttpClient) {}

  login(payload: LoginRequest) {
    return this.http
      .post<LoginResponse>(`${this.baseUrl}/auth/login`, payload)
      .pipe(catchError((err) => this.mapLoginError(err)));
  }

  private mapLoginError(err: unknown) {
    if (err instanceof HttpErrorResponse) {
      if (err.status === 0) {
        return throwError(() => new Error('Impossible de joindre le serveur (Gateway).'));
      }
      if (err.status === 401) {
        return throwError(() => new Error('Email ou mot de passe incorrect.'));
      }
      if (err.status === 403) {
        return throwError(() => new Error("Accès interdit (403)."));
      }
      const msg = typeof err.error === 'string' ? err.error : (err.error?.message as string | undefined);
      return throwError(() => new Error(msg || `Erreur HTTP ${err.status}`));
    }

    return throwError(() => new Error('Erreur inconnue lors du login.'));
  }
}
