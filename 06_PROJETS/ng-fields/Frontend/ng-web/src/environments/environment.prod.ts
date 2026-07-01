export const environment = {
  production: true,
  keycloak: {
    issuer: 'https://auth.ng-fields.ngs.tg/realms/ng-fields',
    clientId: 'ng-fields-web',
    redirectUrl: 'https://ng-fields.ngs.tg/callback',
    postLogoutRedirectUri: 'https://ng-fields.ngs.tg',
    scope: 'openid profile email roles offline_access',
    responseType: 'code',
    silentRenew: true,
    useRefreshToken: true,
    renewTimeBeforeTokenExpiresInSeconds: 30,
  },
  apiUrl: 'https://api.ng-fields.ngs.tg/api',
};
