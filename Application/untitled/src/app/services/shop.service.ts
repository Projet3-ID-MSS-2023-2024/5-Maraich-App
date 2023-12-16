import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable} from "rxjs";
import { Shop } from '../models/shop';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ShopService {

  constructor(private http: HttpClient) {}

  getShopById(id: number): Observable<Shop> {
    return this.http.get<Shop>(`${environment.apiUrl}/shops/get/${id}`);
  }

  getAllShops(): Observable<Shop[]> {
    const url = `${environment.apiUrl}/shops/getAll`;

    return this.http.get<any[]>(url).pipe(
      map(shopsData => shopsData.map(shop => this.mapToShopModel(shop)))
    );
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

}
