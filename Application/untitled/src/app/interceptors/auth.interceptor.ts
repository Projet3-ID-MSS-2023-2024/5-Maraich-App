import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {EMPTY, Observable, of} from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);
  if (router.url !== '/connexion' && router.url !== "/inscription" && !router.url.startsWith("/activation")){
    const token = authService.getTokenFromCookie();

    // Only add the Authorization header if the token is available
    if (token) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      });
      return next(authReq);
    }
  }

  // If it's the login page or if the token is not available, proceed without modification
  return next(req);
};
