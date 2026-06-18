import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  
  const router = inject(Router);
  const token = sessionStorage.getItem('token'); // Verificamos si hay token

  if (token) {
    return true; // El token existe, Madeline puede pasar
  } else {
    // No hay token, lo mandamos al login
    router.navigate(['/login']);
    return false;
  }
};