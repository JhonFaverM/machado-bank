# Autenticación y Manejo de JWT

## Descripción General

El sistema implementa un mecanismo de autenticación basado en **JWT (JSON Web Token)**, permitiendo un modelo **stateless**, donde el backend no mantiene sesiones activas.

El frontend es responsable de almacenar y enviar el token en cada petición protegida.

---

## ¿Por qué JWT?

Se eligió JWT por:

* Escalabilidad (no requiere sesiones en servidor)
* Desacoplamiento entre frontend y backend
* Facilidad de integración con APIs REST
* Seguridad mediante firma digital

---

## Flujo de Autenticación

### 1. Login del usuario

El usuario envía sus credenciales:

```http
POST /api/auth/login
```

### Request Body

```json
{
  "userName": "user",
  "password": "123456"
}
```

---

### 2. Validación en backend

El backend:

* Verifica credenciales
* Genera un token JWT firmado
* Define tiempo de expiración

---

### 3. Respuesta

```json
{
  "userName": "user",
  "role": "CLIENT",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer"
}
```

---

### 4. Almacenamiento del token

El frontend almacena el token en:

```ts
localStorage.setItem('token', res.token);
```

---

### 5. Uso del token

En cada petición protegida:

```http
Authorization: Bearer <token>
```

Esto se maneja automáticamente mediante un **Interceptor HTTP**.

---

## Flujo Interno de una Request

REQUEST → Interceptor → Backend → JWT Filter → Controller

### Detalle:

1. El frontend envía una petición
2. El interceptor agrega el token
3. El backend intercepta con `JwtAuthenticationFilter`
4. Se valida el token
5. Se autentica el usuario en el contexto de seguridad
6. Se ejecuta la lógica del controlador

---

## Interceptor (Frontend)

Responsable de:

* Leer el token desde `localStorage`
* Agregar el header Authorization
* Excluir endpoints públicos (`/api/auth`)

```ts
Authorization: Bearer <token>
```

---

## JwtAuthenticationFilter (Backend)

Encargado de:

* Extraer el token del header
* Validar su firma y expiración
* Obtener el username
* Cargar el usuario desde base de datos
* Registrar autenticación en Spring Security

---

## Manejo de Token Expirado

### Problema detectado

Un token expirado generaba:

```
ExpiredJwtException
```

bloqueando incluso el login.

---

### Solución implementada

Se capturó la excepción en el filtro:

```java
try {
    // lógica JWT
} catch (ExpiredJwtException e) {
    filterChain.doFilter(request, response);
}
```

---

## Exclusión de Endpoints Públicos

El interceptor frontend evita enviar token en:

```bash
/api/auth/**
```

Esto previene errores al intentar autenticarse con tokens antiguos.

---

## Seguridad Implementada

* Tokens firmados (no manipulables)
* Validación en cada request
* Backend stateless
* No uso de sesiones
* Protección mediante Spring Security

---

## Consideraciones Técnicas

* El token contiene:

  * subject (username)
  * issuedAt
  * expiration

* El backend es la única fuente de verdad

* El frontend solo consume y envía el token

---

## Limitaciones actuales

* Token almacenado en localStorage (vulnerable a XSS)
* No hay refresh token implementado
* No hay invalidación de token (blacklist)

---

## Mejoras futuras

* Implementar Refresh Tokens
* Manejo automático de expiración en frontend
* Logout real con invalidación de token
* Uso de HttpOnly cookies (opcional)

---

## Conclusión

El sistema implementa una autenticación moderna basada en JWT, desacoplada y escalable, donde:

* El frontend gestiona el token
* El backend valida cada request
* No existen sesiones persistentes

Este enfoque permite construir aplicaciones seguras y distribuidas siguiendo estándares actuales de desarrollo web.
