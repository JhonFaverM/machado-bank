# 🧠 Machado Bank Frontend - Overview

## 📌 Descripción General

Este proyecto corresponde al frontend de **Machado Bank**, una aplicación web construida con **Angular (standalone components)** que consume un backend desarrollado en **Spring Boot**.

La aplicación permite a los usuarios registrarse, autenticarse mediante JWT y realizar operaciones bancarias como gestión de cuentas y transacciones.

---

## Objetivo

Proveer una interfaz moderna, segura y escalable que:

* Permita autenticación basada en JWT
* Proteja rutas privadas
* Consuma servicios REST del backend
* Mantenga una arquitectura limpia y modular

---

## Tecnologías Utilizadas

* Angular 21 (Standalone Components)
* TypeScript
* HttpClient
* Angular Router
* Interceptors
* Route Guards
* JWT (JSON Web Token)
* LocalStorage (manejo de sesión)

---

## Arquitectura General

El proyecto sigue una arquitectura basada en **separación por features**:

src/app/

* core/ → lógica global (services, guards, interceptors)
* features/ → módulos funcionales (auth, dashboard, accounts)
* shared/ → componentes reutilizables (futuro)

---

## Flujo de Autenticación

1. El usuario se registra o inicia sesión
2. El backend valida credenciales
3. Se genera un token JWT
4. El frontend almacena el token en localStorage
5. Un interceptor agrega el token automáticamente a cada request
6. Un guard protege las rutas privadas
7. El usuario accede a funcionalidades protegidas

---

## Flujo de Petición HTTP

REQUEST → Interceptor → Backend → Response

### Detalle:

1. Angular envía una petición HTTP
2. El interceptor agrega el token JWT (si existe)
3. El backend valida el token
4. Se procesa la lógica de negocio
5. Se retorna la respuesta al frontend

---

## Seguridad

La seguridad se basa en:

* Autenticación mediante JWT
* Uso de Authorization: Bearer Token
* Interceptor para manejo automático del token
* Guard para protección de rutas
* Backend stateless (sin sesiones)

---

## Manejo de Token

* El token se almacena en `localStorage`
* Se envía automáticamente en cada request
* No se envía en endpoints de autenticación (`/api/auth`)
* Puede expirar (manejado en backend)

---

## Navegación

Rutas principales:

* `/login` → acceso público
* `/dashboard` → protegido (requiere autenticación)
* `/account` → protegido

El acceso a rutas protegidas se controla mediante `AuthGuard`.

---

## Componentes Clave

* Login → autenticación de usuario
* Dashboard → vista principal del sistema
* Account → gestión de cuenta del usuario

---

## Principios Aplicados

* Separación de responsabilidades
* Arquitectura por features
* Reutilización de código (interceptor, servicios)
* Seguridad basada en tokens
* Flujo desacoplado frontend-backend

---

## Estado Actual

✔ Login funcional
✔ Comunicación con backend
✔ Manejo de JWT
✔ Interceptor implementado
✔ Guard implementado
✔ Rutas protegidas

---

## Próximos Pasos

* Integración completa del módulo de cuentas
* Implementación de transacciones (depósito, retiro, transferencia)
* Manejo de expiración de token en frontend
* Mejora de UI/UX
* Implementación de logout

---

## 🧾 Notas Finales

Este proyecto sigue buenas prácticas modernas de Angular y simula un entorno real de desarrollo con autenticación segura y arquitectura escalable.

Está diseñado como base para continuar creciendo hacia una aplicación bancaria completa.
