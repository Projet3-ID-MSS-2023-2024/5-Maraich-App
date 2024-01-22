import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Requests} from "../models/requests";
import {Order} from "../models/order";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  apiUrl = environment.apiUrl+"/orders"
  constructor(private http: HttpClient) { }

  getOrders(): Observable<Order[]>  {
    return this.http.get<Order[]>(`${this.apiUrl}/getAll`);
  }

  getOrderById(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/get/${id}`);
  }

  getOrdersByCustomerId(customerId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/get/customer/${customerId}`);
  }

  getOrdersByShopSellerId(shopSellerId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/get/shop/${shopSellerId}`)
  }

  addOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/addOrder`, order);
  }

  updateOrder(order: Order): Observable<Order> {
    return this.http.put<Order>(`${this.apiUrl}/update/order`, order);
  }

  deleteOrderById(id: number) {
    return this.http.delete(`${this.apiUrl}/delete/${id}`);
  }
}
