export const environment = {
  production: false,
  keycloak: {
    issuer: 'http://localhost:8088/realms/ng-fields',
    clientId: 'ng-fields-web',
    redirectUrl: 'http://localhost:4200/callback',
    postLogoutRedirectUri: 'http://localhost:4200',
    scope: 'openid profile email roles offline_access',
    responseType: 'code',
    silentRenew: true,
    useRefreshToken: true,
    renewTimeBeforeTokenExpiresInSeconds: 30,
  },
  apiUrl: 'http://localhost:8080/api',
};
