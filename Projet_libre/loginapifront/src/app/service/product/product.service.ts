import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { Product } from 'src/app/model/Product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private API_URL = environment.API_URL;

  private optionRequete = {
    headers: new HttpHeaders({
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    }), responseType: "text" as "json"
  }

  constructor(private http: HttpClient) { }

  getAllProducts() {
    return this.http.get<Product[]>(this.API_URL+'/catalog');
  }

  getSingleProduct(id: string) {
    return this.http.get<Product>(this.API_URL+'/catalog/'+id);
  }

}
