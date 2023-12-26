import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, map, Observable} from "rxjs";
import {User} from "../models/user";
import {environment} from "../../environments/environment";
import {RankEnum} from "../models/rankEnum";
import {Rank} from "../models/rank";

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
        const url = `${environment.apiUrl}/users/getAll`;

        return this.http.get<any[]>(url).pipe(
            map(usersData => usersData.map(user => this.mapToUserModel(user)))
        );
    }

    getAllRanks(): Observable<Rank[]>{
      const url = `${environment.apiUrl}/users/getAllRanks`;
      return this.http.get<Rank[]>(url);
    }

    private mapToUserModel(data: any): User {
        return {
            idUser: data.idUser,
            firstName: data.firstName,
            surname: data.surname,
            phoneNumber: data.phoneNumber,
            password: data.password,
            address: data.address,
            email: data.email,
            rank: data.rank,
            actif: data.actif,
            orders: data.orders
        };
    }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(`${environment.apiUrl}/users/newUser`, user);
  }

  updateUserAdmin(user: User): Observable<User> {
    const url = `${environment.apiUrl}/users/update/admin`;

    // Map the user data before making the PUT request
    const mappedUser = this.mapToUserModel(user);

    return this.http.put<User>(url, mappedUser).pipe(
      catchError((error: any) => {
        console.error(`Erreur lors de la mise à jour de l'utilisateur : ${error.message || error}`);
        throw error; // relancez l'erreur pour que le composant appelant puisse également la traiter
      })
    );
  }


  updateUserRestricted(updatedUser: User): Observable<User> {
    return this.http.put<User>(`${environment.apiUrl}/users/update/restricted`, updatedUser);
  }

  deleteUserById(id: number): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/users/delete/${id}`);
  }
}
