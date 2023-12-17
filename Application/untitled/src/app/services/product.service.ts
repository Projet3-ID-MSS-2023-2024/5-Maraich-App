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

  updateProduct(product: Product): Observable<any>{
    return this.http.put<Product>(`${this.apiUrl}/update/${product.id}`, product);
  }

  postProduct(product: Product, file : File): Observable<any>{
    const formData: FormData = new FormData();
    formData.append('product', JSON.stringify(product));
    formData.append('file', file, file.name);

    return this.http.post<Product>(`${this.apiUrl}/new`, formData);
  }
}
