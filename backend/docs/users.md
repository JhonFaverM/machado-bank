# Módulo Usuarios

* Módulo Users – Machado Bank

Este documento describe el módulo de Users,
encargado de la autenticación, autorización
y gestión de acceso al sistema bancario.

# 1 📌 Descripción

El módulo Users administra el acceso al sistema mediante credenciales seguras y control de roles.

Es el punto de entrada lógico para cualquier actor dentro del sistema (ADMIN, EMPLOYEE, CLIENT).

Un User permite:

Autenticarse en el sistema mediante userName y password
Definir permisos mediante roles (ADMIN, EMPLOYEE, CLIENT)
Controlar el estado de acceso (enabled / disabled)
Asociarse a un Client en caso de ser un usuario final

Este módulo NO gestiona información financiera directamente,
sino que se encarga de la identidad y control de acceso.

Se implementa cifrado de contraseñas mediante BCrypt (PasswordEncoder de Spring Security)
para garantizar la seguridad de las credenciales.

---

# 2 🏗️ Modelo de Dominio User

Tabla: `users`

| Campo	   | Tipo    | Descripción                                            |
|----------|---------|--------------------------------------------------------|
| id       | Long    | Identificador único generado automáticamente           |
| userName | String	 | Nombre de usuario único                                |
| password | String  | Contraseña cifrada (BCrypt)                            |
| role     | String	 | Rol del usuario (ADMIN / EMPLOYEE / CLIENT)            |
| enabled  | Boolean | Estado del usuario (true = activo / false = bloqueado) |
| client   | Client	 | Cliente asociado (solo aplica para CLIENT)             |

---

# 3 Lógica de Negocio
- El userName debe ser único
- La contraseña SIEMPRE se almacena cifrada
- Todo usuario inicia como enabled = true
- Si el usuario ya existe → se lanza excepción
- No se eliminan usuarios → se deshabilitan (auditoría)
- Usuarios con rol CLIENT deben tener un Client asociado

## 🔗 Relación

User (1) <---> (1) Client

- Un usuario con rol CLIENT debe tener un cliente asociado
- Un cliente pertenece a un único usuario
- ADMIN y EMPLOYEE no requieren Client

##  Registro

El sistema implementa un flujo de registro unificado que crea simultáneamente un User y un Client.

Esto garantiza que todo usuario con rol CLIENT tenga una identidad bancaria asociada.

## 🚀 Endpoint:
POST
/api/auth/register

```json
{
  "userName": "jhon",
  "password": "123",
  "fullName": "Jhon Machado",
  "documentNumber": "83215792",
  "email": "jhon@mail.com"
}
```
# Flujo:
- Validar que el username no exista
- Validar que el documento no esté registrado
- Crear entidad Client
- Crear entidad User
- Asociar User → Client
- Guardar ambos en la base de datos
# Resultado:
- Se crea un usuario autenticable
- Se crea su identidad como cliente
- Se garantiza integridad del modelo

## 🚀 Endpoints
🔹 Crear Usuario (ADMIN)

Descripción: Permite a un administrador crear usuarios en el sistema.

POST
/admin/users

Ejemplo:
POST 
http://localhost:8080/admin/users
- Authorization
Basic Auth
- Username -> admin
- Password -> admin123

```json
{
  "userName": "admin2",
  "password": "123456",
  "role": "ADMIN"
}
```
Respuesta: 200 OK
```json
{
    "id": 6,
    "userName": "admin2",
    "role": "ADMIN",
    "enabled": true,
    "client": null
}
```

### 🔹 Buscar Usuario por userName

# Descripción: Permite consultar un usuario por su identificador lógico.

GET
/admin/users/{userName}

Ejemplo:

GET http://localhost:8080/admin/users/Elimelek -> Sin Body

📌 Respuesta:
👉 200 OK

```JSON
{
    "id": 2,
    "userName": "Elimelek",
    "role": "CLIENT",
    "enabled": true,
    "client": null
}
```

## 🔹 Deshabilitar Usuario

- Descripción: Permite desactivar el acceso de un usuario sin eliminarlo del sistema.

PATCH
/admin/users/{id}/disable

Ejemplo:

PATCH http://localhost:8080/admin/users/3/disable

📌 Respuesta:
👉 200 OK

```JSON
{
    "userId": 3,
    "message": "Usuario deshabilitado correctamente"
}
```
## 🔹 Habilitar Usuario

- Descripción: Permite activar el acceso de un usuario.
- PATCH
  /admin/users/{id}/enable
- Ejemplo:
- http://localhost:8080/admin/users/3/enable

📌 Respuesta:
👉 200 OK

```JSON
{
    "message": "Usuario habilitado correctamente",
    "userId": 3
}
```

⚠️ Validaciones
No se puede crear un usuario con userName duplicado
La contraseña no puede almacenarse en texto plano
Solo ADMIN puede crear o deshabilitar usuarios
Un usuario deshabilitado no puede autenticarse

🔐 Consideraciones de Seguridad
Uso de BCrypt para cifrado de contraseñas
Separación entre autenticación (User) y dominio bancario (Client)
Control de acceso basado en roles
Preparado para futura implementación con JWT

🧪 Pruebas (Postman)

⚠️ Importante:

Todas las peticiones requieren autenticación.

Configuración:

Authorization → Basic Auth
Username: (usuario admin creado en BD)
Password: (contraseña en texto plano)

⚙️ Decisiones Técnicas Destacadas
Uso de @Enumerated(EnumType.STRING) para persistencia de roles
Uso de Optional para evitar errores NullPointerException
Separación de capas (Controller / Service / Repository)
No eliminación de usuarios por razones de auditoría
Uso de PasswordEncoder para seguridad de credenciales
📌 Futuras Mejoras
Implementación de JWT (autenticación moderna)
Extensión del uso de DTOs en todos los endpoints
Manejo global de excepciones
Validaciones con @Valid
Auditoría avanzada de accesos