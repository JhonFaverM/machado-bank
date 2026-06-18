export interface Account {
  id: number;
  accountNumber: string;
  balance: number;
  status: string;
  active: boolean;

  client: {
    id: number;
    documentNumber: string;
    fullName: string;
    email: string;
    status: string;
  };
}