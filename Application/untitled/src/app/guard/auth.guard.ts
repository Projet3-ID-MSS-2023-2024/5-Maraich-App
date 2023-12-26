import {inject} from '@angular/core';
import {ActivatedRoute, CanActivateFn, Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {CookieService} from "ngx-cookie-service";
import {RankEnum} from "../models/rankEnum";

export const authGuard: CanActivateFn = (route, state) => {

  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);
  const cookieService: CookieService = inject(CookieService);
  const isTokenExpired = authService.isTokenExpired();
  const currentRoutePath = route.routeConfig?.path;

  if (isTokenExpired) {
    cookieService.deleteAll();
    if (currentRoutePath !== 'accueil') {
      return router.navigate(['accueil']);
    }
  } else if(authService.userRank === undefined) {
    authService.getRankFromCookie();
  }
  // For access to maraicher panel be maraicher or for access to admin panel be administrator
  if (
    (route.routeConfig && route.routeConfig.path) &&
    (
      (route.routeConfig.path.startsWith('maraicher') && authService.userRank !== RankEnum.MARAICHER && authService.userRank !== RankEnum.ADMINISTRATOR) ||
      (route.routeConfig.path.startsWith('admin') && authService.userRank !== RankEnum.ADMINISTRATOR)
    )
  ) {
    return router.navigate(['accueil']);
  }

  return true;
};
