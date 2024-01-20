import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, timeout} from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';
import {environment} from "../../environments/environment";
import {User} from "../models/user";
import {CookieService} from "ngx-cookie-service";
import {Router} from "@angular/router";
import {RankEnum} from "../models/rankEnum";

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private jwtHelper: JwtHelperService = new JwtHelperService(); // Instanciez le JwtHelperService
  userRank : RankEnum | undefined;
  constructor(private http: HttpClient, private cookieService : CookieService, private route: Router) {}

  login(email: string, password: string): Observable<any> { // Appel API pour le login
    const credentials = { email, password };
    return this.http.post(`${environment.apiUrl}/login`, credentials);
  }

  //Call to the API to register
  signUp(user: User) : Observable<any> {
    return this.http.post(`${environment.apiUrl}/signup`, user);
  }

  //Call to the API for activate a account
  activate(code : string) : Observable<any> {
    const credentials = { code };
    return this.http.post(`${environment.apiUrl}/activation`, credentials);
  }

   logout(){ // Delete the token on the cookie
     this.route.navigate(["/accueil"]);
     this.cookieService.deleteAll()
     this.userRank = undefined;
   }

  getTokenFromCookie(): string | null {
    const accessToken = this.cookieService.get('access_token');

    // Verify if the value is present or not
    return accessToken ? accessToken : null;
  }

  getRankFromCookie() {
    const token = this.getTokenFromCookie()
    if(token)
    {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace('-', '+').replace('_', '/');
      const decodedPayload = JSON.parse(atob(base64));
      let isValidRank = Object.values(RankEnum).includes(decodedPayload.rank as RankEnum);
      if (isValidRank) {
        this.userRank = decodedPayload.rank as RankEnum
      }
    }
  }

  getIdUserFromCookie() {
    const token = this.getTokenFromCookie();
    if (token) {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace('-', '+').replace('_', '/');
      const decodedPayload = JSON.parse(atob(base64));
      const userId = decodedPayload.idUser;
      if (userId) {
        return userId;
      }
    }
  }

  isTokenExpired() {
    const token = this.getTokenFromCookie();
    return token ? this.jwtHelper.isTokenExpired(token) : true;
  }


}
