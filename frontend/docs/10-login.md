# 🔐 Autenticación de Usuario (Login)

## Descripción

Permite a un usuario autenticarse en la aplicación mediante credenciales válidas.  
Si la autenticación es exitosa, el sistema genera un **token JWT** que será utilizado para autorizar las siguientes peticiones.

---
## Endpoint


POST /api/auth/login


---

## Request Body

```json
{
  "userName": "string",
  "password": "string"
}

📤 Response

{
  "token": "string",
  "type": "Bearer",
  "userName": "string",
  "role": "CLIENT | ADMIN"
}

🧠 Descripción de la respuesta

. token → JWT firmado que identifica al usuario
. type → tipo de autenticación (Bearer)
. userName → usuario autenticado
. role → rol asignado en el sistema

 Implementación Frontend

📍 Ubicación
    src/app/features/auth/login/


🔹 Componente Login

Responsabilidades:

. Capturar credenciales del usuario
. Construir el objeto LoginRequest
. Consumir el servicio de autenticación
. Almacenar el token JWT
. Redirigir al dashboard


🔹 Servicio

Archivo:

    src/app/core/services/auth.service.ts

Método:

    login(data: LoginRequest): Observable<AuthResponse>


🔹 Almacenamiento del Token

El token JWT se almacena en:

    sessionStorage

Clave utilizada:

    token


🔁 Flujo de autenticación

Usuario ingresa credenciales
        ↓
POST /api/auth/login
        ↓
Backend valida credenciales
        ↓
Generación de JWT
        ↓
Respuesta con token
        ↓
Frontend guarda token (sessionStorage)
        ↓
Redirección a dashboard


🔐 Uso del Token (JWT)

Una vez autenticado, el token se envía automáticamente en cada petición mediante un interceptor:

Authorization: Bearer <token>


🧩 Interceptor HTTP

Archivo:

src/app/core/interceptors/auth-interceptor.ts

Responsabilidad:

. Adjuntar el token a cada request HTTP
. Excluir endpoints de autenticación (/api/auth)
. Detectar errores 401 (token expirado o inválido)


⚠️ Manejo de expiración

Cuando el token expira:

. El backend responde con 401 Unauthorized
. El interceptor:
.   Elimina el token de sessionStorage
.   Redirige automáticamente al login


🛡️ Seguridad

. Autenticación basada en JWT (stateless)
. No se almacenan sesiones en el servidor
. El token contiene información del usuario (claims)
. El backend valida el token en cada request


📌 Notas

. El login es obligatorio para acceder a rutas protegidas
. El token tiene un tiempo de expiración definido en backend
. El frontend no valida el token directamente, depende del backend