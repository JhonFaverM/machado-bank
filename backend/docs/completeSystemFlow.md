🏦 Machado-Bank

# Flujo de uso y ejecución del sistema

🚀 Inicialización del sistema

El sistema implementa un mecanismo de inicialización automática que
garantiza la existencia de un usuario ADMIN.

Este usuario actúa como punto de entrada y control del sistema con el fin de evitar
estados iniciales inconsistentes, permitir la gestión desde el primer arranque
y asegurar que siempre exista un actor con privilegios administrativos.

# 1. Crear usuario

Una vez levantada la aplicación, el sistema permite el registro de nuevos usuarios mediante:

POST
http://localhost:8080/api/auth/register
🔹 Autenticación
No requiere token
🔹 Request Body

```json
{
   "userName": "Molusca Lopez",
   "password": "123456",
   "fullName": "Molusca Lopez Riscanevo",
   "documentNumber": "1049617222",
   "email": "molusca@mail.com"
}
```

🔹 Comportamiento
- Crea un nuevo usuario
- Se guarda en base de datos
- Se asigna rol CLIENT

🔹 Respuesta
- "Usuario registrado correctamente"

# 2. Login (Autenticación)

POST
http://localhost:8080/api/auth/login

🔹 Autenticación
- No usar BasicAuth
- Credenciales en el body

   🔹 Request Body

```json
{
   "userName": "Molusca Lopez",
   "password": "123456"
   }
```
   
🔹 Respuesta

```json
{
   "userName": "Molusca Lopez",
   "role": "CLIENT",
   "token": "JWT_TOKEN",
   "type": "Bearer"
   }
```

🔹 Uso del token
Authorization: Bearer <token>

#   Flujo interno
   REGISTER → crea usuario  
   LOGIN → genera token  
   REQUEST → envía token  
   FILTER → valida token  
   SECURITY → autentica usuario  
   CONTROLLER → ejecuta lógica


# 3. Crear cuenta

POST
http://localhost:8080/api/accounts/my-account

   🔹 Autenticación
   Authorization: Bearer <token>
   🔹 Request
   No requiere body
   🔹 Comportamiento
   Crea cuenta asociada al usuario autenticado
   Saldo inicial: 0
   Genera accountNumber único
   🔹 Respuesta

```json
 {
   "id": 2,
   "accountNumber": "UUID",
   "balance": 0,
   "status": "ACTIVE",
   "client": {
      "id": 2,
      "documentNumber": "1049617222",
      "fullName": "Molusca Lopez Riscanevo",
      "email": "molusca@mail.com",
      "status": "ACTIVE"
   },
   "active": true
   }
```
  
# 4. Consultar cuenta

GET
http://localhost:8080/api/accounts/{accountNumber}

   🔹 Autenticación
   Authorization: Bearer <token>
   🔹 Comportamiento
   Consulta la cuenta por accountNumber
   Retorna datos de la cuenta


# 5. Depósito

POST
http://localhost:8080/api/v1/transactions/deposit

   🔹 Request Body
```json
{
   "accountNumber": "UUID",
   "amount": 1500000
   }
```
   
   🔹 Comportamiento 
- Incrementa saldo
- Registra transacción tipo DEPOSIT
- Genera referenceCode


# 6. Retiro

POST
http://localhost:8080/api/v1/transactions/withdraw

   🔹 Request Body
```json
{
   "accountNumber": "UUID",
   "amount": 100000.00
   }
```
   
   🔹 Comportamiento
- Valida saldo suficiente
- Descuenta dinero
- Registra transacción tipo WITHDRAW

   🔹 Respuesta
```json
 {
   "referenceCode": "UUID",
   "type": "WITHDRAW",
   "amount": 100000.00,
   "balanceAfter": 2400000.00
   }
```
  
# 7. Transferencia

POST
http://localhost:8080/api/v1/transactions/transfer

   🔹 Request Body
```json
{
   "fromAccountNumber": "UUID",
   "toAccountNumber": "UUID",
   "amount": 100000.00
   }
```
   
   🔹 Comportamiento 
- Valida cuentas
- Valida saldo
- Descuenta origen
- Acredita destino
- Registra transacción tipo TRANSFER

# Reglas del sistema
   Todas las operaciones requieren autenticación excepto:
   /auth/register
   /auth/login

#   El token tiene expiración
- El monto debe ser mayor a 0
- Las cuentas deben estar en estado ACTIVE
- Todas las transacciones generan trazabilidad (referenceCode, balanceAfter)


### Flujo completo del sistema ###
   REGISTER → crea usuario  
   LOGIN → genera token  
   CREATE ACCOUNT → crea cuenta  
   DEPOSIT → agrega saldo  
   WITHDRAW → retira saldo  
   TRANSFER → mueve dinero  
   Estado del sistema

✔ Registro de usuarios
✔ Autenticación con JWT
✔ Gestión de cuentas
✔ Transacciones (depósito, retiro, transferencia)
✔ Seguridad basada en tokens