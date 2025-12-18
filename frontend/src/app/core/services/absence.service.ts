import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AbsenceService {

  private readonly baseUrl = environment.apiUrl;

  constructor(
    private readonly http: HttpClient,
    private readonly tokenService: TokenService
  ) {}

  getMyAbsences() {
    const userId = this.tokenService.getUserId();

    if (!userId) {
      throw new Error('Utilisateur non authentifié (ID manquant)');
    }

    return this.http.get<unknown[]>(
      `${this.baseUrl}/absences/me/${userId}`
    );
  }
}
