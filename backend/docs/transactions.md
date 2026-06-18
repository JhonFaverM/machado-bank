
## 🏦 Módulo de Transacciones

🚀 Características
Transaccionalidad Atómica: Garantía de integridad en transferencias mediante @Transactional.
Seguridad en Capas: Validaciones de formato en DTO Records y reglas de negocio en el Service.
Auditoría Completa: Generación automática de referenceCode (UUID) y rastreo de balanceAfter.
Manejo Global de Errores: Respuestas JSON estandarizadas para excepciones de negocio y validación.
🛠️ Stack Tecnológico
Java 21 (Records & Pattern Matching)
Spring Data JPA (MySQL)
Jakarta Validation (Hibernate Validator)

# 1. Objetivo

* El modulo de Transaction es responsable de gestionar y registrar todos los 
movimientos financieros del sistema bancario.

Este modulo garantiza:

1. Registro historico inmutable de movimientos.

2. Trazabilidad completa del dinero.

3. Integridad y consistencia de saldos.

4. Atomicidad en operaciones críticas como transferencias.

5. Control de concurrencia para evitar corrupción de datos.



# 2. Modelo de Datos

#### 🧾 Entidad Transaction

#### Representa un movimiento financiero realizado sobre una cuenta.

Campos principales:

* ```referenceCode```  → Identificador unico de operacion (clave para auditoria).

* ``` type``` → Tipo de transacción (DEPOSIT, WITHDRAW, TRANSFER_OUT, TRANSFER_IN).

* ```amount``` → Monto del movimiento.

* ```balanceAfter``` → Saldo de la cuenta después del movimiento.

* ```createdAt``` → Fecha del registro.

* ```account``` → Cuenta asociada.

🔎 Proposito clave

#### Transaction funciona como un libro contable (simplificado).

- No se modifica.
- No se elimina.
- Solo se registra.

#### Esto permite trazabilidad del historial completo de una cuenta.



# 3 Deposito (Deposit)
POST
http://localhost:8080/api/v1/transactions/deposit
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Body
```JSON
{
  "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
  "amount": 1000000
}
```
* Respuesta esperada => 200 OK

```json

{
  "id": 10,
  "referenceCode": "cb125904-4179-4a32-aa2f-c6e9c641b74e",
  "type": "DEPOSIT",
  "amount": 1000000,
  "balanceAfter": 1000000.00,
  "description": "Depósito en cuenta",
  "createdAt": "2026-04-15T21:30:40.4523913",
  "account": {
    "id": 4,
    "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
    "balance": 1000000.00,
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
}
```

Flujo:

1. Validar monto.

2. Buscar cuenta.

3. Validar que esté activa.

4. Ejecutar account.credit(amount).

5. Crear Transaction tipo DEPOSIT.

6. Guardar transacción.

Resultado:

* El saldo aumenta.

* Se registra un movimiento histórico.

* Se guarda el saldo posterior.

Trazabilidad:

#### Se puede saber exactamente cuándo y cuánto dinero entro.


# 4. Retiro (Withdraw) 
#### Para hacer RETIROS

POST
http://localhost:8080/api/v1/transactions/withdraw
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Body:
```json
{
    "accountNumber": "caa5deb9-31d1-47d1-afaf-73d346803087",
    "amount": 50000.00
}
```
--- Respuesta:

```json
{
  "id": 11,
  "referenceCode": "258c8586-3bdd-45b0-b484-d9316b946666",
  "type": "WITHDRAW",
  "amount": 50000.00,
  "balanceAfter": 950000.00,
  "description": "Retiro de cuenta",
  "createdAt": "2026-04-15T21:35:24.5897583",
  "account": {
    "id": 4,
    "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
    "balance": 950000.00,
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
}
```
#### Si el retiro supera en balance:

Respuesta => 400 Bad Request

```json
{
  "status": 400,
  "message": "Fondos insuficientes en la cuenta: 85676667-6213-45b5-9e34-9e4634a60242",
  "timestamp": "2026-04-15T21:38:34.1311604"
}
```

#### Si el retiro es igual a 0 o es negativo.
Respuesta => 400 Bad Request
```json
{
  "status": 400,
  "message": "El monto debe ser mayor a cero",
  "timestamp": "2026-04-15T21:39:49.1667404"
}
```

#### Withdraw when is BLOCKED ó CLOSED account

POST
http://localhost:8080/api/v1/transactions/withdraw
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Body:
```json
{
    "accountNumber": "f5689493-6e52-4b1a-85d9-9180e989fb68",
    "amount": 50000.00
}
```

Respuesta => 400 Bad Request

```json
{
    "status": 400,
    "message": "No se puede operar una cuenta en estado: BLOCKED",
    "timestamp": "2026-02-25T22:01:35.2376742"
}
```

Flujo:

1. Validar monto.

2. Buscar cuenta.

3. Validar STATUS.

4. Ejecutar account.debit(amount) (valida fondos).

5. Crear Transaction tipo WITHDRAW.

6. Guardar transacción.

#### Resultado si todo sale bien!!!:

* El saldo disminuye.

* Se registra el movimiento.

* Se evita sobregiro.

#### Trazabilidad:

Se puede saber exactamente cuándo y cuánto dinero salio.



# 5. Transferencia (transaction)

URL
POST
http://localhost:8080/api/v1/transactions/transfer
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Body
```json
{
    "fromAccountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
    "toAccountNumber": "71a8b6aa-d2be-4382-8a33-fc107301e6b6",
    "amount": 5000.00
}
```
Respuesta => 200 OK

```json
{
  "message": "Transferencia exitosa",
  "reference": "26daa419-e51a-4e96-ad79-5c8a254728fc"
}
```
## 5.1 Transferencia a cuenta que no  existe.

Respuesta => 404 Not Found

```json
{
  "fromAccountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
  "toAccountNumber": "71a8b6aa-d2be-4382-8a33-fc107301e6b5",
  "amount": 5000.00
}
```
Response:
```json
{
    "status": 404,
    "message": "Cuenta 71a8b6aa-d2be-4382-8a33-fc107301e6b5 no encontrada",
    "timestamp": "2026-04-15T22:39:25.6396817"
}
```

## 5.2 Transferencia desde cuenta blocked

#### Body

```json
{
    "fromAccountNumber": "f5689493-6e52-4b1a-85d9-9180e989fb68",
    "toAccountNumber": "98727345-1e8a-41b2-a281-8c686568a38d",
    "amount": 5000.00
}
```

Respuesta => 400 Bad Request

Transferencia: esta es la operación mas importante del sistema.

#### Flujo:

1. Validar monto.

2. Verificar que las cuentas sean distintas.

3. Obtener cuenta origen.

4. Obtener cuenta destino.

5. Validar ambas activas.

6. Ejecutar from.debit(amount).

7. Ejecutar to.credit(amount).

8. Generar referenceCode unico (UUID).

9. Crear TRANSFER_OUT para cuenta origen.

10. Crear TRANSFER_IN para cuenta destino.

11. Guardar ambas transacciones.

Resultado:

* Se actualizan ambos saldos.

* Se registran dos movimientos.

* Ambos comparten el mismo referenceCode.

#### 🔎 Importancia del referenceCode

Permite reconstruir una transferencia completa:

* Quien envio.

* Quién recibio.

* Cuánto.

* Cuándo.

* Saldo posterior en ambas cuentas.



# 6. Garantias del Sistema
###   ✅ Atomicidad

#### Uso de @Transactional.

Si algo falla durante una transferencia:

* No se guarda ningun movimiento.

* No quedan saldos inconsistentes.

Se completa la transaccion o nada se completa!!!.

### ✅ Consistencia

Las reglas de negocio estan protegidas en la entidad Account:

* No hay montos negativos.

* No hay sobregiros.

* No hay operaciones sobre cuentas bloqueadas.

### ✅ Concurrencia

#### Uso de @Version en Account.

Esto activa control de concurrencia optimista:

Si dos usuarios intentan modificar el mismo saldo simultaneamente:

* El sistema lanza excepción.

* Se evita corrupción de datos.

### ✅ Auditoria
Base: GET /api/v1/transactions/history/{accountNumber}

#### Consulta historial en Postman

URL
http://localhost:8080/api/v1/transactions/history/85676667-6213-45b5-9e34-9e4634a60242
Authorization => Basic Auth
Username => Madeline (usuario registrado)
Password => 123

Respuesta
```json
[
  {
    "id": 14,
    "referenceCode": "a735689e-8983-4a18-86fa-520c4b9edf14",
    "type": "TRANSFER_OUT",
    "amount": 5000.00,
    "balanceAfter": 940000.00,
    "description": "Transferencia enviada a 71a8b6aa-d2be-4382-8a33-fc107301e6b6",
    "createdAt": "2026-04-15T22:38:18.188409",
    "account": {
      "id": 4,
      "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
      "balance": 940000.00,
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
  },
  {
    "id": 12,
    "referenceCode": "26daa419-e51a-4e96-ad79-5c8a254728fc",
    "type": "TRANSFER_OUT",
    "amount": 5000.00,
    "balanceAfter": 945000.00,
    "description": "Transferencia enviada a 71a8b6aa-d2be-4382-8a33-fc107301e6b6",
    "createdAt": "2026-04-15T22:33:41.609735",
    "account": {
      "id": 4,
      "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
      "balance": 940000.00,
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
  },
  {
    "id": 11,
    "referenceCode": "258c8586-3bdd-45b0-b484-d9316b946666",
    "type": "WITHDRAW",
    "amount": 50000.00,
    "balanceAfter": 950000.00,
    "description": "Retiro de cuenta",
    "createdAt": "2026-04-15T21:35:24.589758",
    "account": {
      "id": 4,
      "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
      "balance": 940000.00,
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
  },
  {
    "id": 10,
    "referenceCode": "cb125904-4179-4a32-aa2f-c6e9c641b74e",
    "type": "DEPOSIT",
    "amount": 1000000.00,
    "balanceAfter": 1000000.00,
    "description": "Depósito en cuenta",
    "createdAt": "2026-04-15T21:30:40.452391",
    "account": {
      "id": 4,
      "accountNumber": "85676667-6213-45b5-9e34-9e4634a60242",
      "balance": 940000.00,
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
  }
]
```

Cada transaccion guarda:

* Tipo

* Monto

* Saldo posterior

* Fecha

* Referencia unica

Nunca se elimina historial.

Esto permite:

* Auditorias financieras.

* Reconstrucción historica.

* Reportes por rango de fecha.

* Consulta por referencia.



# 🔐 7. Seguridad en Transacciones
📌 Principio de Seguridad

* El módulo de transacciones implementa un modelo de seguridad basado en:

* Autenticación obligatoria
* Validación de propiedad (ownership)
* Protección en la capa de servicio

⚠️ Importante:

* El sistema NO confía en datos enviados por el cliente (frontend).

# 🔑 Autenticación

Todas las operaciones requieren un usuario autenticado mediante Basic Auth.

Ejemplo en Postman:

Authorization → Basic Auth
Username: usuario
Password: contraseña

# * Obtención del Cliente Autenticado

El sistema utiliza AuthService para obtener el cliente logueado:
```
var client = authService.getAuthenticatedClient();
```
Este cliente es la identidad REAL que el sistema usa para validar operaciones.

# 🔥 Validación de Propiedad (Ownership)

Antes de ejecutar cualquier operación financiera, el sistema valida que:

* La cuenta pertenece al cliente autenticado

Ejemplo conceptual:
``` 
if (!account.getClient().getId().equals(clientId)) {
    throw new SecurityException("No tienes acceso a esta cuenta");
}
```

# 🏦 *** Aplicación en Operaciones ***
# Depósito
Se valida que la cuenta pertenece al cliente
Se impide depositar en cuentas ajenas

# Retiro
* Se valida propiedad
* Se valida saldo
* Se valida estado de cuenta

# Transferencia
* Se valida SOLO la cuenta de origen
* No se valida la cuenta destino

✔ Permite transferencias a terceros
✔ Replica comportamiento bancario real

# 🔍 Historial de Transacciones

* El acceso al historial está protegido:
  Solo el propietario puede consultar
  Se valida ownership antes de devolver datos

# ⚠️ Riesgos Mitigados
* Este modelo previene:

Acceso a cuentas de otros usuarios
Manipulación de peticiones HTTP
Transferencias fraudulentas
Consulta indebida de historial

# 🧱 Seguridad en Capas **
La seguridad se implementa en múltiples niveles:

# 1. Controller
Obtiene usuario autenticado
No confía en el request
# 2. Service (CRÍTICO)
Valida propiedad
Aplica reglas de negocio
Protege operaciones
# 3. Base de Datos
Integridad mediante relaciones
Control de concurrencia (@Version)


# 🚀 Importancia Arquitectónica

La seguridad NO depende del tipo de autenticación (Basic Auth o JWT).
# La protección real está en:
* Validaciones de negocio
* Control de acceso a recursos


# 8. Decisiones de Diseño
### 1️⃣ Separación de responsabilidades

* Account maneja estado actual.

* Transaction maneja historial.

### Ledger simplificado

No se recalcula saldo desde transacciones.
El saldo vive en la cuenta.


### Transferencias con doble registro

Se registran dos movimientos en lugar de uno.

Esto permite:

* Auditoría clara.

* Separación de perspectiva (origen y destino).



# 9. Nivel del Modelo

Este diseño implementa:

* Control transaccional.

* Registro histórico inmutable.

* Concurrencia optimista.

* Trazabilidad completa.

* Protección de reglas de negocio.

* Verificación de ownership