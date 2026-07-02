class ApiConstants {
  static const baseUrl = String.fromEnvironment(
    'API_URL',
    defaultValue: 'http://localhost:8081',
  );
  static const apiPrefix = '/api';
  static const authEndpoint = '$apiPrefix/public/auth';
  static const interventionsEndpoint = '$apiPrefix/interventions';
  static const clientsEndpoint = '$apiPrefix/clients';
  static const usersEndpoint = '$apiPrefix/users';

  static const tokenKey = 'auth_token';
  static const refreshTokenKey = 'refresh_token';
  static const userKey = 'auth_user';
}
