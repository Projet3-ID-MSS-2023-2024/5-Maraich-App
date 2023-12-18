import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Shop } from '../models/shop';
import { environment } from '../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ShopService {
  apiUrl = environment.apiUrl + "/shop";

  constructor(private http:HttpClient) { }

    getShopByOwnerId(idOwner: number){
      return this.http.get<Shop>(`${this.apiUrl}/shop/owner/${idOwner}`);
    }

  getShopById(id: number): Observable<Shop> {
    return this.http.get<Shop>(`${environment.apiUrl}/shops/get/${id}`);
  }

  getAllShops(): Observable<Shop[]> {
    const url = `${environment.apiUrl}/shops/getAll`;

    return this.http.get<Shop[]>(url).pipe(
      map(shopsData => shopsData.map(shopData => this.mapToShopModel(shopData)))
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
      enabled: data.enabled,
      owner: data.owner,
      orders: data.orders,
      products: data.products
    };
  }
}
