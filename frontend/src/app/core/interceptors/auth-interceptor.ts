import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';


export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  // No enviar token en login/register
  if (req.url.includes('/api/auth')) {
    return next(req);
  }

  const token = sessionStorage.getItem('token');

  let request = req;

  // Agregar token si existe
  if (token) {
    request = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  return next(request).pipe(

    catchError((error) => {

      // AQUÍ detectamos expiración
      if (error.status === 401) {

        console.warn('Token expirado o inválido');

        // limpiar sesión
        sessionStorage.removeItem('token');

        // redirigir a login
        router.navigate(['/login']);
      }

      return throwError(() => error);
    })
  );
};