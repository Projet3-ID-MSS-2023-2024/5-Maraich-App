import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/user";
import {environment} from "../../environments/environment";
import {RankEnum} from "../models/rankEnum";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${environment.apiUrl}/users/get/${id}`);
  }

  getUsersByRank(rank: RankEnum): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/users/getByRank/${rank}`);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/users/getAll`);
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/users/newUser`, user);
  }

  updateUserAdmin(user: User): Observable<User> {
    return this.http.put<User>(`${environment.apiUrl}/users/update/admin`, user);
  }

  updateUserRestricted(updatedUser: User): Observable<User> {
    return this.http.put<User>(`${environment.apiUrl}/users/update/restricted`, updatedUser);
  }

  deleteUserById(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/users/delete/${id}`);
  }
}
