# 🛡️ Módulo Security – Machado Bank

---
Este módulo define la estrategia de seguridad del sistema,
incluyendo autenticación, autorización y protección de la lógica de negocio.
---
# 1 📌 Descripción

El módulo de seguridad garantiza que únicamente usuarios autenticados
y autorizados puedan acceder a los recursos del sistema.

# Responsabilidades principales:

- Autenticar usuarios mediante Basic Auth
- Gestionar el contexto de seguridad (Spring Security)
- Proteger operaciones críticas
- Validar propiedad de recursos (ownership)
- Evitar accesos indebidos a información sensible

# 2 🏗️ Arquitectura de Seguridad

El sistema implementa seguridad en múltiples capas:

Cliente → Controller → Service → Base de Datos
↓
AuthService
↓
SecurityContext (Spring)


# 3 🔐 Autenticación

El sistema utiliza Basic Auth como mecanismo de autenticación.

🔹 Flujo
1. Usuario envía credenciales (Authorization Header)
2. Spring Security valida credenciales
3. Se genera un Authentication
4. Se almacena en SecurityContextHolder


# 4 🔗 AuthService (Puente de Seguridad)

El servicio AuthService permite acceder al usuario autenticado
sin depender directamente de Spring Security.

Funciones principales:
- Obtener username autenticado
- Obtener entidad User
- Obtener Client asociado

Uso típico:
```java
var client = authService.getAuthenticatedClient();
```

# 5 🧠 Autorización (Regla Clave del Sistema)

La autorización NO se basa únicamente en roles, sino en:

🔴 Ownership (propiedad del recurso)
🔥 Validación de Ownership

```java
private void validateOwnership(Account account, Long clientId) {
    if (!account.getClient().getId().equals(clientId)) {
        throw new SecurityException("No tienes acceso a esta cuenta");
    }
}
```

📌 ¿Qué protege?
- Acceso a cuentas
- Transferencias
- Historial de transacciones
- Operaciones financieras

# ⚠️ Regla fundamental

- Un usuario SOLO puede operar sobre sus propias cuentas.

# 6 🧪 Aplicación por Capas
🎯 Controller

Responsabilidad:

- Obtener usuario autenticado
- NO confiar en datos del cliente

Ejemplo:
```java
var client = authService.getAuthenticatedClient();

```

# 🧠 Service (Capa Crítica)

Aquí ocurre la seguridad real.

# Responsabilidad:

- Validar ownership
- Validar estado de entidades
- Ejecutar lógica segura

👉 Aunque el controller falle, el sistema sigue protegido.


# 7 🔄 Flujo Completo de Seguridad
1. Usuario envía credenciales (Basic Auth)
2. Spring autentica usuario
3. Usuario se guarda en SecurityContext
4. AuthService obtiene User
5. Controller obtiene Client
6. Service valida ownership
7. Se ejecuta la operación


# 8 🔁 Caso Especial: Transferencias
- Regla aplicada:
   - Se valida SOLO la cuenta origen
-   Justificación:

👉 Permite transferencias a terceros, replicando sistemas bancarios reales.


# 9 🔍 Consultas Seguras
🔹 Historial por cuenta
- Requiere ownership
- Solo el dueño puede consultar

🔹 Consulta por referencia
- Puede retornar múltiples transacciones
- Se valida que TODAS pertenezcan al cliente


# 10 ⚠️ Riesgos Mitigados
- Acceso a cuentas de otros usuarios
- Manipulación de requests
- Consultas indebidas
- Transferencias no autorizadas


# 11 ⚙️ Decisiones Técnicas
🔹 Basic Auth
- Implementación simple inicial
- Permite entender el flujo completo

🔹 AuthService
- Desacopla seguridad de negocio
- Centraliza acceso al usuario autenticado

🔹 Seguridad en Services
- Protección real del sistema
- Independiente del controller


# 12 Pruebas en postman
# 🔐 Configuración de Autenticación

Todas las peticiones requieren autenticación mediante Basic Auth.

Configuración:
- Authorization → Basic Auth
- Username: usuario registrado (ej: juan123)
- Password: contraseña en texto plano (ej: 123456)

# 📥 Ejemplo 1: Registro de Usuario
Request

- POST http://localhost:8080/api/auth/register
Body (JSON)
```json
{
  "userName": "Madeline",
  "password": "123",
  "fullName": "Madeline Lopez",
  "documentNumber": "10496386371",
  "email": "madeline@mail.com"
}
```
# Response
```json
{
  "message": "Usuario registrado correctamente"
}
```

# 🏦 Ejemplo 2: Crear Cuenta
Request
POST http://localhost:8080/api/accounts/my-account
Body

❌ No requiere body

Response
```json
{
  "id": 1,
  "accountNumber": "uuid-generado",
  "balance": 0,
  "status": "ACTIVE"
}
```

# 📊 Ejemplo 3: Consultar Mis Cuentas
Request
GET 
http://localhost:8080/api/accounts/my-accounts
Response
````json
[
    {
        "id": 3,
        "accountNumber": "71a8b6aa-d2be-4382-8a33-fc107301e6b6",
        "balance": 945000.00,
        "status": "ACTIVE",
        "client": {
            "id": 4,
            "documentNumber": "83215793",
            "fullName": "Jhon Machado",
            "email": "jhonfaver@mail.com",
            "status": "ACTIVE"
        },
        "active": true
    }
]
````

# 🔍 Ejemplo 4: Consultar Cuenta por Número
Request
GET 
http://localhost:8080/api/accounts/{accountNumber}
Response
```json
{
    "id": 3,
    "accountNumber": "71a8b6aa-d2be-4382-8a33-fc107301e6b6",
    "balance": 945000.00,
    "status": "ACTIVE",
    "client": {
        "id": 4,
        "documentNumber": "83215793",
        "fullName": "Jhon Machado",
        "email": "jhonfaver@mail.com",
        "status": "ACTIVE"
    },
    "active": true
}
```

# 🔄 Ejemplo 5: Historial de Transacciones
Request
GET http://localhost:8080/api/v1/transactions/history/{accountNumber}
Response

👉 200 OK

# 🔐 Ejemplo 6: Bloquear Cuenta
Request
PATCH 
http://localhost:8080/api/accounts/{accountNumber}/block
Response
```json
{
    "message": "Cuenta 71a8b6aa-d2be-4382-8a33-fc107301e6b6 bloqueada",
    "timestamp": "2026-04-15T14:26:31.620258700"
}
```

# ⚠️ Errores Comunes

# 401 Unauthorized
- No enviaste credenciales
- Credenciales incorrectas
# 400 Bad Request
- Usuario sin cliente asociado
- Datos incompletos en el request
# 403 Forbidden
Intento de acceder a recursos de otro cliente
# 🧠 Nota Importante

- Nunca se envía clientId desde el frontend
- El sistema lo obtiene automáticamente desde el usuario autenticado
- Todas las validaciones críticas ocurren en el backend


# 12 🚀 Evolución del Sistema

El sistema está preparado para:

- JWT (JSON Web Token)
- Control de roles más avanzado
- Auditoría de seguridad


# 13 🧠 Conclusión

La seguridad en el sistema no se limita a la autenticación, sino que se
implementa directamente en la lógica de negocio mediante validaciones de ownership.

Esto garantiza:
- Accesos controlados
- Operaciones legítimas
- Protección ante manipulación externa