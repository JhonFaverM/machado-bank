import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TransactionRequestModel } from '../../models/transaction-request.model';
import { Transaction } from '../../models/transaction.model';
import { TransferRequestModel } from '../../models/transfer-request.model';
import { TransferResponse } from '../../models/transfer-response.model';

@Injectable({
  providedIn: 'root',
})

export class TransactionService {

   // Importante incluir el /v1/ que definiste en el Controller
  private API = `${environment.apiUrl}/v1/transactions`;

  constructor(private http: HttpClient) { }

  // *** DEPÓSITO ***
  deposit(dto: TransactionRequestModel): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.API}/deposit`, dto);
  }

  // *** RETIRO ***
  withdraw(dto: TransactionRequestModel): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.API}/withdraw`, dto);
  }

  // *** TRANSFERENCIA ***
  // Retorna un Map con mensaje y referencia según tu Controller
  transfer(dto: TransferRequestModel): Observable<TransferResponse> {
    return this.http.post<TransferResponse>(`${this.API}/transfer`, dto);
  }

  // *** HISTORIAL ***
  getHistory(accountNumber: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.API}/history/${accountNumber}`);
  }

  // *** CONSULTA POR REFERENCIA ***
  getByReference(referenceCode: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.API}/reference/${referenceCode}`);
  }

}
