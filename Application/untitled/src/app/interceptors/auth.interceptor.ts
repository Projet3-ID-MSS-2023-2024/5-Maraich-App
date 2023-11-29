import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService : AuthService = inject(AuthService);
  const token = authService.getTokenFromCookie();
  const authReq = req.clone({
    headers: req.headers.set('Authorization',`Bearer ${token}`),
  });
  return next(authReq);
};
