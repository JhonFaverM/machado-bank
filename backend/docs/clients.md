# Módulo Clients – Machado Bank

Este documento describe el **módulo de Clients** del proyecto **Machado Bank**, 
su propósito, estructura, endpoints disponibles y decisiones tecnicas tomadas durante el desarrollo.

---

## 1. Propósito del módulo

El modulo **Clients** es la base del sistema. Representa a 
las personas que pueden tener cuentas bancarias y realizar transacciones.

Un **Client** puede:

* Registrarse en el sistema
* Ser consultado por ID o número de documento
* Ser actualizado
* Ser bloqueado, activado y cerrado.
* Ser eliminado
* Tener una o varias **cuentas** asociadas

---

## 2. Modelo de dominio: Client

Clase ubicada en:

```
com.machado.bank.model.Client
```

### Propiedades

| Campo          | Tipo          | Descripción                                   |
|----------------|---------------|-----------------------------------------------|
| id             | Long          | Identificador único (PK)                      |
| documentNumber | String        | Número de documento (único)                   |
| fullName       | String        | Nombre completo del cliente                   |
| email          | String        | Email único                                   |
| status         | String        | Estado del cliente (ACTIVE / BLOKED / CLOSED) |
| createdAt      | LocalDateTime | Fecha de creación (automática)                |

### Decisiones

* Se usan **getters y setters manuales** (Lombok fue descartado)
* El constructor vacio es obligatorio para JPA
* El constructor con parametros se usa en tests
* El estado (status) está encapsulado. No se permite su modificación
mediante Setters, sino a traves de metodos de dominio especificos
(block, close, activate), asegurando que la logica de negocio 
no sea vulnerada por la API publica.

---

## 3. Repositorio: ClientRepository

Ubicación:

```
com.machado.bank.repository.ClientRepository
```

Extiende:

```
JpaRepository<Client, Long>
```

### Métodos personalizados

```java
Optional<Client> findByDocumentNumber(String documentNumber);
Optional<Client> findByEmail(String email);
```

### ¿Por que no se ven los demas mtodos?

Porque **JpaRepository ya provee**:

* findAll()
* findById()
* save()
* deleteById()

Spring Data JPA los implementa automaticamente.

---

## 4. Servicio

### Interfaz: IClientService

Define el contrato del módulo:

```java
List<Client> findAll();
Page<Client> findAllPaged(Pageable pageable);
Client findById(Long id);
Client findByDocumentNumber(String documentNumber);
Client create(Client client);
Client update(Long id, Client client);
Client blockClient(Long id);
Client closeClient(Long id);
Client activateClient(Long id);
void deleteById(Long id);
```

### Implementación: ClientServiceImpl

Responsabilidades:

* Contiene la **logica de negocio**
* Valida reglas (ej: email unico)
* Lanza excepciones cuando algo no existe

Ejemplo clave:

```
if (clientRepository.findByEmail(client.getEmail()).isPresent()) {
    throw new DuplicateResourceException("Email ya registrado");
}
```

---

## 5. Controlador REST

Ubicación:

```
com.machado.bank.controller.ClientController
```

Base URL:

```
/admin/clients
```

### Endpoints

#### Crear cliente por admin

```
  POST
  http://localhost:8080/admin/clients
```
Body:
```json
{
  "documentNumber": "12345",
  "fullName": "Gerardo admin client",
  "email": "pruebaPrueba@mail.com"
}
```
Al intentar crear un client y si el documento o el email ya existe
Lanza un status 409, email o documento ya existe
---

#### Buscar por ID codigo interno del banco
Authorization -> Basic Auth
Username => admin
Password => admin123
```
GET
http://localhost:8080/admin/clients/2
```
Si el ID no existe se lanza un status 404.
---

#### Buscar por documento

```
GET 
http://localhost:8080/admin/clients/document/123456
```
Al listar un cliente por documento que no existe:
Lanza un stattus 404. Cliente no encontrado.

---

#### Listar clientes con paginación

```
GET
http://localhost:8080/admin/clients?page=0&size=5
```
#### page=1 -> Equivale a pagina #2
Los clientes son listados de 5 por pagina y en orden ascendente.

```
{
    "content": [ 
        { "id": 1, "fullName": "Juanito", ... },
        { "id": 2, "fullName": "Pedro", ... }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 5
    },
    "totalElements": 50,  // Cuantos clientes hay en total en la DB
    "totalPages": 10,     // Cuantas paginas existen en total
    "last": false,        // ¿Es la ultima pagina?
    "first": true         // ¿Es la primera pagina?
}
```
---

#### Actualizar cliente

```
PUT
http://localhost:8080/admin/clients/8
```
Body (solo campos permitidos):

```json
{
  "fullName": "Juanito Alimaña Modificado",
  "email": "juanito_nuevo@mail.com"
}
```
Si intenta modificar cliente no existente:
Lanza un status 404, cliente no encontrado.

---

## 6. Estados de un cliente
En los siguientes estados al ejecutar alguno de los metodos se retorna
mensaje del cambio de estado, como tambien queda relacionado la fecha
del cambio en el servicio.

#### 6.1 blocked => Seguridad
PATCH http://localhost:8080/admin/clients/{id}/block

Se bloquea un cliente por asuntos administrativos

#### 6.2 close => Relación finaliza
PATCH http://localhost:8080/admin/clients/{ID}/close

Finaliza relación banco con cliente.

#### 6.3 activate => Se reestablece relacion
PATCH http://localhost:8080/admin/clients/{id}/activate

Se activa relación cliente banco.

---

#### Eliminar cliente

```
DELETE
http://localhost:8080/admin/clients//{id}
```
Respuestas:
Si encuentra el cliente => Lanza un status 200 OK
Si no existe cliente => status 404 "Cliente no encontrado"

---

## 7. Manejo de errores global.

### ResourceNotFoundException

Se lanza cuando:

* No existe un cliente por ID
* No existe un cliente por documento

### GlobalExceptionHandler
* DuplicateResourceException (409 Conflict): Se lanza cuando el email
o el documento ya existen en la base de datos.

* Por alguna razon las excepciones de errores anteriores no captura
  el error.

Ejemplo de error:

```json
{
  "status": 404,
  "message": "Cliente no encontrado",
  "timestamp": "2026-02-10T21:30:00"
}
```

---

# Porque el diseño asi?

* Trazabilidad: Cada cambio de estado genera un log en el servidor para auditoría.
* Integridad: Se usa validación previa (Check-before-save) 
para evitar colisiones de datos.
* UX (Experiencia de Usuario): Se usan códigos HTTP específicos 
(404, 409, 200) para que el Frontend sepa exactamente qué error mostrar.

---

✍️ Documento personal de aprendizaje – Machado Bank
