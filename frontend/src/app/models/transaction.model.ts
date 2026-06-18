export interface Transaction {
  id: number;
  referenceCode: string;
  type: 'DEPOSIT' | 'WITHDRAW' | 'TRANSFER'; // Coincidiendo con tus Enums de Java
  amount: number;
  balanceAfter: number;
  description?: string; // El '?' indica que es opcional
  createdAt: string | Date; // El JSON lo manda como string, pero podemos usarlo como Date
  
  // En el historial, el backend suele mandar el objeto Account 
  // o al menos su información básica.
  account?: {
    id: number;
    accountNumber: string;
  };
}
