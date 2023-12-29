import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Reservation} from "../models/reservation";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  apiUrl = environment.apiUrl + "/reservations";
  constructor(private http: HttpClient) { }

  addReservation(quantityReserve: number, idProduct: number, idUser: number) {
      const credentials = {idUser: idUser, idProduct: idProduct, reserveQuantity: quantityReserve};
      return this.http.post(`${this.apiUrl}/addReservation`, credentials);
  }

  getShoppingCart(idUser : number) {
    return this.http.get<Reservation[]>(`${this.apiUrl}/getShoppingCartUser/` + idUser);
  }

  deleteReservation(idReservation : number) {
    return this.http.delete(`${this.apiUrl}/delete/` + idReservation)
  }

  updateReservation(idReservation: number, newQuantity: number) {
    const credentials = {idReservation: idReservation, newQuantity: newQuantity}
    return this.http.put(`${this.apiUrl}/update`, credentials)
  }

  deleteShoppingCartByUserId(idUser: number) {
    return this.http.delete(`${this.apiUrl}/deleteShoppingCart/` + idUser)
  }

  existShoppingCart(idUser : number, idShop : number) {
    return this.http.get(`${this.apiUrl}/existShoppingCart/` + idUser + "/" + idShop);
  }
}
