import { environment } from '../../../environments/environment';
import { OpenIdConfiguration } from 'angular-auth-oidc-client';

export const authConfig: OpenIdConfiguration = {
  authority: environment.keycloak.issuer,
  redirectUrl: environment.keycloak.redirectUrl,
  postLogoutRedirectUri: environment.keycloak.postLogoutRedirectUri,
  clientId: environment.keycloak.clientId,
  scope: environment.keycloak.scope,
  responseType: environment.keycloak.responseType,
  silentRenew: environment.keycloak.silentRenew,
  useRefreshToken: environment.keycloak.useRefreshToken,
  renewTimeBeforeTokenExpiresInSeconds: environment.keycloak.renewTimeBeforeTokenExpiresInSeconds,
  postLoginRoute: '/dashboard',
};
