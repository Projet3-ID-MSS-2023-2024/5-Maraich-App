import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Shop} from "../models/shop";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ShopService {
  apiUrl = environment.apiUrl + "/shop";

  constructor(private http:HttpClient) { }

    getShopByOwnerId(idOwner: number){
      return this.http.get<Shop>(`${this.apiUrl}/shop/owner/${idOwner}`);
    }
}
