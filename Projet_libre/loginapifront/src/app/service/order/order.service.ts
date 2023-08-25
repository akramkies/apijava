import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private API_URL = environment.API_URL;
  private products: ProductResponseModel[] = [];

  constructor(private http: HttpClient,) { }

  getSingleOrder(orderId: number) {
    return this.http.get<ProductResponseModel>(this.API_URL + '/command/' + orderId).toPromise();
  }
}


interface ProductResponseModel {
  id: string,
  family: string,
  designation: string,
  salePrice: number,
  quantityOrdred: number,
  imgUrl: string
}