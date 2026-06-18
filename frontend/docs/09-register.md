# Registro de Usuario

## Descripción

Permite a un usuario crear una cuenta en la aplicación mediante el endpoint de autenticación.

---

## Endpoint


POST /api/auth/register


---

## Request Body

```json
{
  "userName": "string",
  "password": "string",
  "fullName": "string",
  "documentNumber": "string",
  "email": "string"
}

 Response

{
  "message": "Usuario registrado correctamente"
}

    Implementación Frontend

Ubicación

src/app/features/auth/register/
 Componente

Encargado de:

Capturar datos del formulario
Construir el objeto RegisterRequest
Consumir el servicio de autenticación
Redirigir al login tras registro exitoso

 Servicio

Archivo:

src/app/core/services/auth.service.ts

Método:

register(data: RegisterRequest): Observable<RegisterResponse>

 Flujo
Usuario accede a Login
        ↓
Selecciona "Registrarse"
        ↓
Completa formulario
        ↓
POST /api/auth/register
        ↓
Usuario creado en base de datos
        ↓
Redirección a Login


Seguridad
Endpoint público (no requiere autenticación)
No se envía token JWT en esta petición
Controlado por configuración de seguridad en backend

 Notas
El registro no genera sesión automáticamente
El usuario debe autenticarse posteriormente vía /login
Validaciones adicionales se manejan en backend