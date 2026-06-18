import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';
import { Account } from '../../models/account.model';

@Injectable({
  providedIn: 'root',
})
export class AccountService {

  private API = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) {}

  // 🏦 Crear cuenta
  createAccount(): Observable<Account> {
    return this.http.post<Account>(`${this.API}/my-account`, {});
  }

  // 🔍 Obtener una cuenta
  getAccount(accountNumber: string): Observable<Account> {
    return this.http.get<Account>(`${this.API}/${accountNumber}`);
  }

  // 📄 Listar cuentas
  getMyAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.API}/my-accounts`);
  }

}