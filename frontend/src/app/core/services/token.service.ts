import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly TOKEN_KEY = 'jwt_token';
  private readonly USER_ID_KEY = 'user_id';

  // 🔐 TOKEN
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  clearToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // 👤 USER ID  ✅ AJOUT ICI
  setUserId(id: string): void {
    localStorage.setItem(this.USER_ID_KEY, id);
  }

  getUserId(): string | null {
    return localStorage.getItem(this.USER_ID_KEY);
  }

  clearUserId(): void {
    localStorage.removeItem(this.USER_ID_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  clearAll(): void {
    this.clearToken();
    this.clearUserId();
  }
}
