import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Shop } from '../model/shop';


@Injectable({
  providedIn: 'root'
})
export class ShopService {

  private apiUrl = 'http://localhost:8080/api/shop';

  constructor(private http: HttpClient) {}

  getShops(): Observable<Shop[]> {
    return this.http.get<Shop[]>(this.apiUrl);
  }

}
