import { Injectable } from '@angular/core';
import { HttpClient } from  '@angular/common/http';
import { LoginRequest } from '../../models/login-request.model';
import { AuthResponse } from '../../models/auth-response.model';
import { RegisterRequest } from '../../models/register-request.model';
import { RegisterResponse } from '../../models/register-response.model';
import { environment } from '../../../environments/environments';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private API = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient, 
    private router: Router
  ) {}

  login(data: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API}/login`, data);
  }

  logout() {
    // 1. Limpiamos todo el rastro de la sesión
    sessionStorage.clear(); 
    
    // 2. Mandamos al usuario de vuelta al login
    this.router.navigate(['/login']);
  }
  
  register(data: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.API}/register`, data);
  }

}
