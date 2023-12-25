import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable} from "rxjs";
import { Shop } from '../models/shop';
import { environment } from '../../environments/environment';
import {User} from "../models/user";
@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http: HttpClient) {}

  getShopById(id: number): Observable<Shop> {
    return this.http.get<Shop>(`${environment.apiUrl}/shops/get/${id}`);
  }

  getShopByOwnerId(idOwner: number){
    return this.http.get<Shop>(`${environment.apiUrl}/shop/owner/${idOwner}`);
  }

  getAllShops(): Observable<Shop[]> {
    const url = `${environment.apiUrl}/shop/getAll`;

    return this.http.get<any[]>(url).pipe(
      map(shopsData => shopsData.map(shop => this.mapToShopModel(shop)))
    );
  }

  updateShop(shop: Shop): Observable<Shop> {
    const url = `${environment.apiUrl}/shop/update`;
    // Map the user data before making the PUT request
    const mappedShop = this.mapToShopModel(shop);

    return this.http.put<Shop>(url, mappedShop).pipe(
        catchError((error: any) => {
          console.error(`Erreur lors de la mise à jour du shop : ${error.message || error}`);
          throw error; // relancez l'erreur pour que le composant appelant puisse également la traiter
        })
    );
  }

  deleteShop(id: number): Observable<void> {
    const url = `${environment.apiUrl}/shop/delete/${id}`;
    return this.http.delete<void>(url);
  }

  private mapToShopModel(data: any): Shop {
    return {
      idShop: data.idShop,
      name: data.name,
      email: data.email,
      address: data.address,
      picture: data.picture,
      description: data.description,
      shopIsOkay: data.shopIsOkay,
      enable: data.enable,
      owner: data.owner,
      orders: data.orders,
      products: data.products
    };
  }

  postImage(file: File){
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<{fileName : string}>(`${environment.apiUrl}/images/upload`, formData);
  }

}
