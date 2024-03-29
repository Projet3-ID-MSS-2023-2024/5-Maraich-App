import { HttpInterceptorFn } from '@angular/common/http';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {EMPTY, Observable, of} from "rxjs";
import {CookieService} from "ngx-cookie-service";
import {environment} from "../../environments/environment";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);
  const cookieService : CookieService = inject(CookieService);
  const requestUrl = req.url;
  if (!requestUrl.startsWith(`${environment.apiUrl}/products/get-all-by-shop`) && !requestUrl.startsWith(`${environment.apiUrl}/categories/get`)
    && !requestUrl.startsWith(`${environment.apiUrl}/images/getImage`)
    && requestUrl !== `${environment.apiUrl}/login`
    && requestUrl !== `${environment.apiUrl}/shop/getAll`
    && requestUrl !== `${environment.apiUrl}/signup` && !requestUrl.startsWith(`${environment.apiUrl}/activation`)){
    const token = authService.getTokenFromCookie();
    if(authService.isTokenExpired()){
      cookieService.deleteAll();
      authService.userRank = undefined;
      return EMPTY;
    }
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
