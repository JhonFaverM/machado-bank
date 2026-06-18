# 🏦 Machado Bank API

Proyecto backend desarrollado con **Spring Boot** como ejercicio práctico
para aprender arquitectura backend, REST APIs y persistencia de datos.

---

## 🚀 Tecnologías usadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Postman

> Lombok NO se usa (decisión consciente para entender el modelo real).

---

## 📦 Arquitectura

El proyecto sigue una arquitectura por capas:

- **Controller** → expone endpoints REST
- **Service** → lógica de negocio
- **Repository** → acceso a datos (JPA)
- **Model** → entidades del dominio
- **Exception** → manejo global de errores

---

## 📚 Documentación
La documentación funcional y técnica del proyecto se encuentra en la carpeta `/docs`:

- [Clientes](docs/clientes.md)
- [Cuentas](docs/cuentas.md)
- [Transacciones](docs/transacciones.md)

## ▶️ Ejecución
1. Configurar la base de datos MySQL
2. Ejecutar el proyecto desde el IDE o con:

```bash
mvn spring-boot:run