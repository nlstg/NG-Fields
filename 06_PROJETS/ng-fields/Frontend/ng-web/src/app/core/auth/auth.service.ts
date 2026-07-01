import { Injectable } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { Observable, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private oidc: OidcSecurityService) {}

  get isAuthenticated$(): Observable<boolean> {
    return this.oidc.isAuthenticated$.pipe(map((r) => r.isAuthenticated));
  }

  get userData$(): Observable<any> {
    return this.oidc.userData$;
  }

  get token$(): Observable<string> {
    return this.oidc.getAccessToken();
  }

  login(): void {
    this.oidc.authorize();
  }

  logout(): void {
    this.oidc.logoff().subscribe();
  }

  checkAuth(url?: string): Observable<any> {
    return this.oidc.checkAuth(url);
  }
}
