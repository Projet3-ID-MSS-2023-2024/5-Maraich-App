import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {Product} from "../models/product";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Category} from "../models/category";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  apiUrl = environment.apiUrl + "/products";

  constructor(private http: HttpClient) { }

  getProduct(): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiUrl}/getAll`);
  }

  getProductByCategories(category: Category): Observable<Product[]>{
    return this.http.post<Product[]>(`${this.apiUrl}/getAllByCategories`, { params: { category: category } });
  }

  getProductByShop(id:number): Observable<Product[]>{
    return this.http.get<Product[]>(`${this.apiUrl}/get-all-by-shop/${id}`);
  }

  updateProduct(product: Product): Observable<any>{
    return this.http.put<Product>(`${this.apiUrl}/update/${product.id}`, product);
  }

  postProduct(product: Product): Observable<Product>{
    return this.http.post<Product>(`${this.apiUrl}/new`, product);
  }

  postImage(file: File){
      const formData = new FormData();
      formData.append('file', file);

      return this.http.post<{fileName : string}>(`${environment.apiUrl}/images/upload`, formData);
  }

  deleteProduct(productId:number){
    return this.http.delete(`${this.apiUrl}/delete/${productId}`);
  }
}
