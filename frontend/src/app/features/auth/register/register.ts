import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { RegisterRequest } from '../../../models/register-request.model';
import { RegisterResponse } from '../../../models/register-response.model';


@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  userName = '';
  password = '';
  fullName = '';
  documentNumber = '';
  email = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register() {

    const data: RegisterRequest = {
      userName: this.userName,
      password: this.password,
      fullName: this.fullName,
      documentNumber: this.documentNumber,
      email: this.email
    };

     this.authService.register(data).subscribe({
      next: (res: RegisterResponse) => {

        alert(res.message);

        // redirigir a login
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al registrar usuario');
      }
    });
  } 



}
