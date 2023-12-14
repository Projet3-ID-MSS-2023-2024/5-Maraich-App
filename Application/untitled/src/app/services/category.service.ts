import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Category} from "../models/category";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  apiUrl = environment.apiUrl+"/categories";

  constructor(private http: HttpClient) { }

  getCategories(): Observable<Category[]>{
    return this.http.get<Category[]>(`${this.apiUrl}/getAll`);
  }

  getCategoryById(id: number): Observable<Category>{
    return this.http.get<Category>(`${this.apiUrl}/get/${id}`)
  }

  updateCategory(category: Category): Observable<any>{
    return this.http.put(`${this.apiUrl}/update/${category.idCategory}`, category);
  }

  postCategory(category : Category): Observable<Category>{
    return this.http.post<Category>(`${this.apiUrl}/new`, category);
  }

  deleteCategory(id: number){
    return this.http.delete<Category>(`${this.apiUrl}/delete/${id}`)
  }
}
