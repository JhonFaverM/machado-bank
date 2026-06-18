# Interceptor JWT

Intercepta todas las peticiones HTTP y agrega el token.

## Reglas

- No aplica a /api/auth
- Agrega header Authorization

Authorization: Bearer <token>

## Beneficio

Evita repetir lógica en cada request