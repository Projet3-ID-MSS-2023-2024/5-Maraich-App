import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, timeout} from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';
import {environment} from "../../environments/environment";
import {User} from "../models/user";
import {CookieService} from "ngx-cookie-service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private jwtHelper: JwtHelperService = new JwtHelperService(); // Instanciez le JwtHelperService
  constructor(private http: HttpClient, private cookieService : CookieService) {}

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

   logout(){ // Supprimez le cookie du token en l'expirant
    let returnObservable = this.http.get(`${environment.apiUrl}/disconnection`);
    setTimeout(() => {this.cookieService.deleteAll()}, 2000)
    return returnObservable;
  }

  getTokenFromCookie(): string | null {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
      const [name, value] = cookie.split('=').map(part => part.trim());
      if (name.toLowerCase() === 'access_token') {
        return value.replace(/^"(.*)"$/, '$1') || null;
      }
    }
    return null;
  }

  isTokenExpired() {
    const token = this.getTokenFromCookie();
    return token ? this.jwtHelper.isTokenExpired(token) : true;
  }


}
