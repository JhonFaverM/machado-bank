import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../../models/login-request.model';
import { AuthResponse } from '../../../models/auth-response.model';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  userName = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {
    const data: LoginRequest = {
      userName: this.userName,
      password: this.password
    };

    this.authService.login(data).subscribe({
      next: (res: AuthResponse) => {
        // guardar token
        sessionStorage.setItem('token', res.token);
        sessionStorage.setItem('userName', res.userName);

        console.log('TOKEN RECIBIDO:', res.token);
        console.log('USUARIO LOGUEADO:', res.userName);

        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error(err);
        alert('Error en login');
      }
    });
  }
}