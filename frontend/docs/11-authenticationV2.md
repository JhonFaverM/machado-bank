# 🔐 Sistema de Autenticación (JWT)

## 📌 Descripción

La aplicación implementa un sistema de autenticación basado en **JSON Web Tokens (JWT)**.  
El backend genera un token tras el login exitoso, el cual es utilizado por el frontend para autorizar las peticiones a recursos protegidos.

---

## 🧠 Arquitectura

El sistema de autenticación sigue un enfoque **stateless**:

``` id="7p8qz3"
Frontend → envía credenciales
Backend → valida usuario
Backend → genera JWT
Frontend → almacena token
Frontend → envía token en cada request
Backend → valida token en cada petición


🔑 Flujo de Autenticación


REGISTER → crea usuario
LOGIN → genera token
REQUEST → envía token
FILTER → valida token (backend)
SECURITY → autentica usuario
CONTROLLER → ejecuta lógica


🔗 Endpoints involucrados

Registro
POST /api/auth/register
``` id="8nh1fn"

---

### Login


POST /api/auth/login


---

## 🔐 Token JWT

El backend genera un token con información del usuario:

``` id="gj6h9r"
- subject (userName)
- issuedAt (iat)
- expiration (exp)

El token es firmado y validado en cada request.

🧩 Implementación Frontend
  📍 Almacenamiento

El token se almacena en:

sessionStorage

Clave:

token


🔁 Interceptor HTTP

Archivo:

src/app/core/interceptors/auth-interceptor.ts

Responsabilidades
. Adjuntar el token a cada request HTTP
. Excluir endpoints públicos (/api/auth)
. Manejar errores de autenticación

Comportamiento
Si existe token:
    → Se añade header Authorization

Authorization: Bearer <token>


⚠️ Manejo de expiración

Cuando el token expira o es inválido:

Backend → responde 401 Unauthorized

El interceptor:

- Elimina token del sessionStorage
- Redirige al login


🛡️ Guard de Rutas

Archivo:

src/app/core/guards/auth.guard.ts

Responsabilidad
. Proteger rutas privadas
. Verificar existencia del token

Comportamiento
Si existe token:
    → Permite acceso

Si no existe:
    → Redirige a /login


🧭 Rutas protegidas
/dashboard
/account

Estas rutas requieren autenticación previa.


🔐 Seguridad
. Autenticación basada en JWT (stateless)
. No se utilizan sesiones en servidor
. Validación del token en cada request
. Separación de responsabilidades (frontend/backend)


📌 Consideraciones

. El frontend no valida el contenido del token
. La validez del token depende exclusivamente del backend
. El token tiene un tiempo de expiración definido en backend


🔁 Flujo completo

Usuario se registra
        ↓
Login con credenciales
        ↓
Recepción de JWT
        ↓
Almacenamiento en sessionStorage
        ↓
Interceptor añade token a requests
        ↓
Backend valida token
        ↓
Acceso a recursos protegidos


🚀 Próximos pasos

Implementar logout manual
Manejo avanzado de errores
Control de roles (CLIENT / ADMIN)
Refresh token (opcional)