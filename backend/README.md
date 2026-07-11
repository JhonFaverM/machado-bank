# 🏦 Machado Bank API

Backend de una aplicación bancaria digital desarrollado con **Java 17** y **Spring Boot 3**.

Machado Bank nació como un proyecto personal con el propósito de comprender cómo se construye una aplicación backend moderna utilizando Java, aplicando conceptos de arquitectura por capas, seguridad, persistencia de datos y operaciones bancarias.

Más que desarrollar un banco completo, el objetivo fue entender cómo interactúan tecnologías como Spring Boot, Spring Security, JWT, JPA/Hibernate y MySQL dentro de un mismo proyecto.

---

# 🎯 Objetivos del proyecto

Este proyecto fue desarrollado con los siguientes objetivos:

- Comprender el funcionamiento de una arquitectura backend basada en Spring Boot.
- Implementar autenticación y autorización utilizando JWT.
- Aprender el manejo de Spring Security.
- Construir una API REST siguiendo buenas prácticas.
- Comprender el funcionamiento de las transacciones bancarias.
- Implementar operaciones que requieren consistencia en los saldos.
- Integrar un backend Java con un frontend desarrollado en Angular.
- Preparar una aplicación para su despliegue en la nube.

---

# 🚀 Tecnologías utilizadas

### Backend

- Java 17
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- JWT (JSON Web Token)
- Maven

### Base de datos

- MySQL
- Clever Cloud

### Frontend

- Angular 21

### Herramientas

- IntelliJ IDEA
- Visual Studio Code
- Postman
- Git
- GitHub

> Este proyecto no utiliza Lombok por decisión del autor, con el propósito de comprender completamente el funcionamiento de las entidades, constructores, getters y setters en Java.

---

# 📂 Arquitectura

Machado Bank implementa una arquitectura por capas.

```
               Angular

                  │

            REST API (HTTP)

                  │

             Controllers

                  │

              Services

                  │

           Repositories

                  │

               MySQL
```

Cada capa tiene una responsabilidad específica.

- **Controller**
    - Expone los endpoints REST.

- **Service**
    - Contiene la lógica de negocio.

- **Repository**
    - Gestiona el acceso a la base de datos mediante Spring Data JPA.

- **Model**
    - Representa las entidades del dominio.

- **Security**
    - Gestiona autenticación, autorización y validación de JWT.

- **Exception**
    - Centraliza el manejo de errores de la aplicación.

---

# 🔐 Seguridad

La autenticación se realiza mediante **JSON Web Token (JWT)**.

Flujo general:

```
Cliente

↓

Login

↓

Spring Security

↓

JWT

↓

Authorization: Bearer Token

↓

Endpoints protegidos
```

Los usuarios autenticados reciben un token que debe enviarse en cada solicitud protegida.

Actualmente existen los siguientes roles:

- ADMIN
- EMPLOYEE
- CLIENT

Las operaciones bancarias están protegidas mediante Spring Security y autorización basada en roles.

---

# 💳 Funcionalidades implementadas

Actualmente Machado Bank permite:

## Clientes

- Registro de usuarios
- Inicio de sesión
- Gestión de roles
- Asociación Usuario - Cliente

## Cuentas

- Crear cuenta bancaria
- Consultar cuentas propias
- Buscar cuenta por número
- Bloquear cuenta
- Activar cuenta
- Cerrar cuenta

## Transacciones

- Depósitos
- Retiros
- Transferencias entre cuentas
- Validación de saldo
- Historial completo de movimientos

---

# 📚 Documentación

La documentación técnica del proyecto se encuentra en la carpeta **/docs**.

Actualmente incluye documentación sobre:

- Clientes
- Cuentas
- Transacciones

---

# ⚙️ Variables de entorno

Para ejecutar el proyecto es necesario definir las siguientes variables de entorno.

| Variable | Descripción |
|----------|-------------|
| DB_HOST | Servidor MySQL |
| DB_PORT | Puerto MySQL |
| DB_NAME | Nombre de la base de datos |
| DB_USER | Usuario de la base de datos |
| DB_PASSWORD | Contraseña de la base de datos |
| JWT_SECRET | Clave utilizada para firmar los JWT |

---

# ▶️ Ejecución local

1. Clonar el repositorio.

```bash
git clone https://github.com/tu-usuario/machado-bank.git
```

2. Configurar las variables de entorno.

3. Ejecutar la aplicación desde IntelliJ IDEA o mediante Maven Wrapper.

```bash
./mvnw spring-boot:run
```

4. El backend quedará disponible en:

```
http://localhost:8080
```

---

# 📈 Próximas mejoras

El proyecto continuará evolucionando.

Entre las mejoras previstas se encuentran:

- Despliegue del backend en Render
- Despliegue del frontend en Vercel
- Docker
- Pruebas unitarias
- CI/CD
- Recuperación de contraseña
- Notificaciones
- Microservicios (objetivo de aprendizaje futuro)

---

# 👨‍💻 Autor

**Jhon Machado**

Machado Bank es un proyecto desarrollado como laboratorio personal de aprendizaje para comprender el funcionamiento de aplicaciones backend construidas con Java y Spring Boot, aplicando conceptos de arquitectura, seguridad y persistencia de datos.

El nombre del proyecto hace referencia al apellido de su autor y representa el proceso de aprendizaje construido paso a paso durante su desarrollo.