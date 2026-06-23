import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

export type UserRole = 'ADMINISTRATEUR' | 'OPERATEUR' | 'FOURNISSEUR';

export interface AuthSession {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  role: UserRole;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignupRequest {
  nom: string;
  prenom: string;
  email: string;
  password: string;
  role: UserRole;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  userId: number;
  role: UserRole;
}

export interface RefreshResponse {
  accessToken: string;
  tokenType: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  readonly apiUrl = 'http://localhost:8089/SpringMVC/auth';
  private readonly sessionKey = 'pms_auth_session';

  constructor(private http: HttpClient) {}

  login(payload: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap((response) => this.storeSession(response))
    );
  }

  signup(payload: SignupRequest): Observable<unknown> {
    return this.http.post<unknown>(`${this.apiUrl}/signup`, payload);
  }

  refreshAccessToken(): Observable<RefreshResponse> {
    const refreshToken = this.getRefreshToken();

    return this.http.post<RefreshResponse>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap((response) => this.storeAccessToken(response))
    );
  }

  logoutRequest(): Observable<unknown> {
    return this.http.post<unknown>(`${this.apiUrl}/logout`, { refreshToken: this.getRefreshToken() });
  }

  logout(): void {
    localStorage.removeItem(this.sessionKey);
  }

  getAccessToken(): string | null {
    return this.getSession()?.accessToken ?? null;
  }

  getRefreshToken(): string | null {
    return this.getSession()?.refreshToken ?? null;
  }

  getRole(): UserRole | null {
    return this.getSession()?.role ?? null;
  }

  getUserId(): number | null {
    return this.getSession()?.userId ?? null;
  }

  isAuthenticated(): boolean {
    return Boolean(this.getAccessToken() && this.getRefreshToken());
  }

  hasRole(roles: UserRole[]): boolean {
    const currentRole = this.getRole();

    return Boolean(currentRole && roles.includes(currentRole));
  }

  private getSession(): AuthSession | null {
    const rawSession = localStorage.getItem(this.sessionKey);

    if (!rawSession) {
      return null;
    }

    try {
      return JSON.parse(rawSession) as AuthSession;
    } catch {
      this.logout();
      return null;
    }
  }

  private storeSession(response: LoginResponse): void {
    localStorage.setItem(this.sessionKey, JSON.stringify(response));
  }

  private storeAccessToken(response: RefreshResponse): void {
    const session = this.getSession();

    if (!session) {
      return;
    }

    localStorage.setItem(this.sessionKey, JSON.stringify({
      ...session,
      accessToken: response.accessToken,
      tokenType: response.tokenType
    }));
  }
}
