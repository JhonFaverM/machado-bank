# 📘 Módulo Cuentas
Módulo Accounts – Machado Bank
Este documento describe el módulo de Accounts,
encargado de la gestión de productos financieros 
vinculados a los clientes.

# 1 📌 Descripción
El módulo Accounts administra el estado financiero de los clientes. 
Es el puente entre el cliente y sus movimientos de dinero.
Una Account permite:
Ser creada vinculada a un Client existente.
Consultar saldo en tiempo real.
Controlar estados de operatividad (ACTIVE, BLOCKED, CLOSED).
Garantizar la integridad de los saldos mediante Bloqueo Optimista.


Uso de JPA con bloqueo optimista (@Version) para garantizar
la consistencia en transacciones simultaneas. Ademas, se
aplica encapsulamiento haciendo que el setBalance sea privado, 
forzando a que toda modificación de saldo pase por la logica 
de negocio de los metodos credit y debit.

Una cuenta pertenece a un unico cliente.

---

# 2 🏗️ Modelo de Dominio Account

Tabla: `accounts`

| Campo         | Tipo       | Descripción                                       |
|---------------|------------|---------------------------------------------------|
| id            | Long       | Identificador unico interno, generado por sistema |
| accountNumber | String     | Número unico de cuenta (UUID)                     |
| balance       | BigDecimal | Saldo actual                                      |
| Version       | Long       | Control de concurrencia (@Version) Evita perdida  |
| status        | String     | Estado de la cuenta (ACTIVE / BLOCKED / CLOSED)   |
| client        | Client     | Cliente propietario                               |

---

# 3 Lógica de Negocio

- El número de cuenta se genera automáticamente con UUID.
- El balance inicia en 0.
- Toda cuenta inicia como ACTIVE.
- Si el cliente no existe → se lanza ResourceNotFoundException.
- Encapsulamiento: El saldo NO se puede modificar directamente. 
Solo se altera mediante los métodos de dominio credit() y debit().

---

## 🔗 Relación

Cliente (1) <---> (varias) Cuenta

Un cliente puede tener varias cuentas.  
Una cuenta pertenece a un solo cliente.

---

## 🚀 Endpoints

### Crear Cuenta
Descripción: Crea una cuenta de ahorros para el cliente especificado.
Respuesta (201 Created): Retorna el objeto Account con su número generado.

Ejemplo:

POST
http://localhost:8080/api/accounts/my-account
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123
Bearer Token => copiar token devuelto cuando se loggueo

No requiere body (la cuenta se crea con balance 0 y estado ACTIVE).

Respuesta: 201 Created

```json
{
  "id": 4,
  "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
  "balance": 0,
  "status": "ACTIVE",
  "client": {
    "id": 5,
    "documentNumber": "10496386371",
    "fullName": "Madeline Lopez",
    "email": "madeline@mail.com",
    "status": "ACTIVE"
  },
  "active": true
}
```

---

### Buscar Cuenta por Número
Descripción: Obtiene los detalles de una cuenta específica.
Error (404): Si el número de cuenta no existe.
Ejemplo:

GET
http://localhost:8080/api/accounts/85676667-6213-45b5-9e34-9e4634a60242
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Respuesta: 200 OK

```json
{
  "id": 4,
  "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
  "balance": 0.00,
  "status": "ACTIVE",
  "client": {
    "id": 5,
    "documentNumber": "10496386371",
    "fullName": "Madeline Lopez",
    "email": "madeline@mail.com",
    "status": "ACTIVE"
  },
  "active": true
}
```

---

### Listar Cuentas por Cliente
Descripción: Devuelve la lista de todos los productos 
financieros de un cliente.
Ejemplo:

GET
http://localhost:8080/api/accounts/my-accounts
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Respuesta con existencia:
```JSON
[
  {
    "id": 4,
    "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
    "balance": 0.00,
    "status": "ACTIVE",
    "client": {
      "id": 5,
      "documentNumber": "10496386371",
      "fullName": "Madeline Lopez",
      "email": "madeline@mail.com",
      "status": "ACTIVE"
    },
    "active": true
  }
]
```
Comportamiento: Si el cliente existe pero no tiene cuentas,
devuelve una lista vacía [].
Respuesta sin existencia:

---

###  5 Blocked Cuenta
Descripcion:
Por razones de seguridad el cliente puedo recurrir
a BLOQUEAR la cuenta de su propiedad, mediante este metodo
el banco puede bloquear la cuenta.

Ejemplo:

PATCH
http://localhost:8080/api/accounts/85676667-6213-45b5-9e34-9e4634a60242/block
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

#### Respuesta:

200 OK

```json
{
  "message": "Cuenta 85676667-6213-45b5-9e34-9e4634a60242 bloqueada",
  "timestamp": "2026-04-15T18:19:24.655452300"
}
```

###  4 Activar Cuenta
Descripcion:
Por razones de seguridad el cliente pudo recurrir
a bloquear la cuenta de su propiedad, mediante este metodo
el banco puede ACTIVAR la cuenta.

Ejemplo:

PATCH
http://localhost:8080/api/accounts/85676667-6213-45b5-9e34-9e4634a60242/block
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

#### Respuesta:
200 OK
```json
{
  "message": "Cuenta 85676667-6213-45b5-9e34-9e4634a60242 activada",
  "timestamp": "2026-04-15T18:21:34.225161500"
}
```

###  5 Cerrar Cuenta
Descripcion:
Por razones de seguridad y administrativa el cliente puedo recurrir
a CERRAR cuenta
Ejemplo:

PATCH
http://localhost:8080/api/accounts/85676667-6213-45b5-9e34-9e4634a60242/close
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

#### Respuesta:
200 OK

```json
{
  "message": "Cuenta 85676667-6213-45b5-9e34-9e4634a60242 cerrada",
  "timestamp": "2026-04-15T18:25:50.186163"
}
```

### Decisiones Técnicas Destacadas
Uso de BigDecimal: Se utiliza para evitar errores de redondeo 
decimal en cálculos financieros.
UUID: Se opto por identificadores universales para los números 
de cuenta, aumentando la seguridad y evitando que sean predecibles.
Optimistic Locking: Implementado para asegurar que, 
en entornos de alta concurrencia, el saldo siempre sea consistente.
