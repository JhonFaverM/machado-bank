## 📄 auth.service.ts

### 📌 Ubicación
core/services/

### 🎯 Responsabilidad
Encargado de manejar la autenticación con el backend.

### ⚙️ Métodos

#### login(data)
- Envía credenciales al backend
- Retorna token JWT

#### register(data)
- Registra nuevo usuario

### 🔗 Relación con otros componentes
- Usado por: login.ts
- Conectado con: interceptor (indirectamente)