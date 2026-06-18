🏦 Account Component

📌 Descripción

El componente Account es responsable de gestionar las cuentas del usuario autenticado en el frontend.

Permite:

. Obtener las cuentas desde el backend
. Crear nuevas cuentas
. Mostrar información de forma segura (enmascarando datos sensibles)


🧠 Estado del Componente

accounts: AccountModel[] = [];
loading = false;
showBalance = true;

- accounts: Lista de cuentas del usuario (fuente de verdad en la UI).
- loading: Controla el estado de carga mientras se consultan datos.
- showBalance: Permite alternar la visibilidad del saldo.


⚙️ Dependencias

    constructor(
    private accountService: AccountService,
    private cdr: ChangeDetectorRef
    ) {}

- AccountService: Maneja la comunicación con el backend.
- ChangeDetectorRef: Permite forzar la actualización de la vista en casos      donde Angular no detecta cambios automáticamente.


🔄 Carga de Cuentas

    loadAccounts()

Flujo

1. Activa el estado de carga.
2. Solicita las cuentas al backend mediante AccountService.
3. Al recibir la respuesta:
   - Asigna los datos al estado del componente.
   - Desactiva el loading.
   - Fuerza la actualización de la vista.

Implementación

    loadAccounts() {
    this.loading = true;

    this.accountService.getMyAccounts().subscribe({
        next: (res) => {
        this.accounts = [...res];
        this.loading = false;
        this.cdr.detectChanges();
        },
        error: (err) => {
        console.error(err);
        this.loading = false;
        }
    });
    }

- Nota técnica

El uso de detectChanges() asegura que la vista se actualice inmediatamente tras recibir datos.
En condiciones normales, Angular debería detectar estos cambios automáticamente.


🏦 Crear Cuenta

    createAccount()

Flujo

1. Envía una solicitud al backend para crear una cuenta.
2. El usuario se identifica mediante el token JWT (no se envían datos adicionales).
3. Tras la creación:
    - Se notifica al usuario.
    - Se recargan las cuentas para mantener consistencia.

Implementación

    createAccount() {
    this.accountService.createAccount().subscribe({
        next: () => {
        this.loadAccounts();
        },
        error: (err) => {
        console.error(err);
        }
    });
    }

Decisión de diseño

No se modifica el estado local manualmente.
Se vuelve a consultar el backend para garantizar que la UI refleje la fuente real de datos.


🔐 Formateo de Número de Cuenta

    formatAccountNumber(accountNumber: string): string

Descripción

Devuelve una versión enmascarada del número de cuenta, mostrando únicamente los últimos caracteres.

Implementación

    formatAccountNumber(accountNumber: string): string {
    if (!accountNumber) return '';
    const last5 = accountNumber.slice(-5);
    return `****${last5}`;
    }

Propósito

- Proteger información sensible
- Mejorar la presentación en la UI


🎯 Responsabilidades del Componente

- Consumir servicios del backend relacionados con cuentas
- Gestionar estado de carga
- Mantener sincronización con la API
- Ejecutar acciones del usuario (crear cuenta)


Formatear datos para presentación

🚀 Consideraciones

- El componente utiliza Standalone Components
- La autenticación se maneja mediante JWT
- Las peticiones HTTP incluyen automáticamente el token vía Interceptor
- La fuente de verdad siempre es el backend