import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  apiUrl = environment.apiUrl + "/images";

  constructor(private http:HttpClient) { }

  getImage(fileName: string): Observable<Blob> {
    const imageUrl = `${this.apiUrl}/${fileName}`;
    return this.http.get(imageUrl, { responseType: 'blob' });
  }
}
