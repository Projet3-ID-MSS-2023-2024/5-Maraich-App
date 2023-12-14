import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {

  const authService : AuthService = inject(AuthService);
  const router: Router = inject(Router);

  const isTokenExpired = authService.isTokenExpired();

  if (isTokenExpired) {
    return router.navigate(['/connexion']);
  }

  return true;
};
