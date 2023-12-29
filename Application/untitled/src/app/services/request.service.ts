import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Requests} from "../models/requests";

@Injectable({
  providedIn: 'root'
})
export class RequestService {
  apiUrl = environment.apiUrl+"/requests"
  constructor(private http: HttpClient) { }

  getRequests(): Observable<Requests[]>{
    return this.http.get<Requests[]>(`${this.apiUrl}/getAll`);
  }
  getRequestById(id: number): Observable<Requests>{
    return this.http.get<Requests>(`${this.apiUrl}/get/${id}`);
  }
  addRequest(request: Requests): Observable<Requests>{
    return this.http.post<Requests>(`${this.apiUrl}/addRequest`, request);
  }

  updateRequest(request: Requests): Observable<Requests>{
    return this.http.put<Requests>(`${this.apiUrl}/update/request`, request);
  }

  deleteRequestById(id: number) {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
}
