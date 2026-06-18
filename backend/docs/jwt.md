🔐 Módulo de Seguridad – JWT

Machado Bank

Este documento describe la implementación de seguridad basada en JWT
(JSON Web Token), encargada de la autenticación y autorización de usuarios dentro del sistema.

1 📌 Descripción General

El sistema implementa un modelo de seguridad stateless, donde:

No se usan sesiones
Cada petición HTTP es independiente
La autenticación se basa en un token JWT
2 🧩 Componentes del Módulo
🔐 AuthController

Responsable de:

Registro de usuarios
Autenticación (login)
Generación de token JWT
🧠 JwtService

Responsable de:

Generar tokens
Extraer información del token
Validar tokens
👤 CustomUserDetailsService

Responsable de:

Cargar usuario desde base de datos
Adaptarlo a Spring Security (UserDetails)
🚧 JwtAuthenticationFilter

Responsable de:

Interceptar cada request
Extraer el token del header
Validar el token
Autenticar al usuario en el contexto de seguridad
⚙️ SecurityConfig

Responsable de:

Configurar reglas de seguridad
Definir endpoints públicos y protegidos
Registrar el filtro JWT
Desactivar sesiones (STATELESS)
3 🔄 Flujo Completo de Seguridad (JWT)
🔐 1. Registro
POST /api/auth/register
Se crea:
Client
User
Se guarda en base de datos
🔑 2. Login
POST /api/auth/login
http://localhost:8080/api/auth/login
Request
{
"userName": "jhon",
"password": "123"
}
Flujo interno
AuthController
AuthenticationManager
CustomUserDetailsService
Validación de credenciales
JwtService.generateToken()
Response
{
"token": "eyJhbGciOiJIUzI1NiJ9...",
"type": "Bearer",
"userName": "jhon",
"role": "CLIENT"
}
📡 3. Peticiones protegidas
Header requerido
Authorization: Bearer <token>
🔍 Flujo interno en cada request
Llega request al servidor
JwtAuthenticationFilter intercepta
Extrae token del header
JwtService.extractUsername()
CustomUserDetailsService carga usuario
JwtService.isTokenValid()
Se crea Authentication
Se guarda en SecurityContext
🎯 Resultado
El usuario queda autenticado automáticamente
Los controllers pueden acceder al usuario autenticado
4 🔐 Reglas de Seguridad
Todas las rutas requieren autenticación excepto:
/api/auth/**
Rutas administrativas:
/admin/** → ROLE_ADMIN
Sistema sin sesiones:
SessionCreationPolicy.STATELESS
5 🧠 Uso en la lógica de negocio

El sistema NO confía en el frontend.

Ejemplo:

var client = authService.getAuthenticatedClient();

✔ El usuario se obtiene desde el token
❌ No se recibe desde el request

6 🔥 Validaciones implementadas

✔ Usuario autenticado obligatorio
✔ Token válido en cada request
✔ Validación de ownership en servicios
✔ Roles para control de acceso
✔ Protección contra manipulación del frontend

7 ⚠️ Riesgos mitigados
Acceso no autorizado
Suplantación de identidad
Manipulación de requests
Acceso a recursos de otros usuarios
8 🧪 Pruebas en Postman
🔑 Login
POST /api/auth/login

Body:

{
"userName": "jhon",
"password": "123"
}
📡 Usar token

En cada request:

Authorization → Bearer Token
Bearer eyJhbGciOiJIUzI1NiJ9...
✅ Ejemplo
GET /api/accounts/my-accounts

✔ Con token → 200 OK
❌ Sin token → 401 Unauthorized

9 🏗️ Decisiones Técnicas
❌ Eliminación de Basic Auth

Se reemplaza completamente por JWT

✔ Stateless
No sesiones
Escalable
Ideal para frontend moderno (Angular)
✔ Separación de responsabilidades
Componente	Responsabilidad
Controller	Entrada
Filter	Seguridad
Service	Lógica
JwtService	Token
10 🚀 Evolución futura

El sistema está preparado para:

Refresh Tokens
Expiración avanzada
Blacklist de tokens
OAuth2 / OpenID
Microservicios
11 🧠 Conclusión

La seguridad en Machado Bank:

No depende del frontend
Está centralizada en backend
Es escalable
Sigue buenas prácticas reales de la industria

💥 Traducción simple:

👉 El login genera un token
👉 El token viaja en cada request
👉 El backend valida TODO
👉 Nadie accede sin permiso