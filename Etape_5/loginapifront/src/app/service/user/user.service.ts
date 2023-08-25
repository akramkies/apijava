import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Address } from 'src/app/model/Address';
import { User } from 'src/app/model/User';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private API_URL = environment.API_URL;

  private optionRequete = {
    headers: new HttpHeaders({
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    }), responseType: "text" as "json"
  }

  constructor(
    private http: HttpClient
  ) { }

  getUserInfo(){
    return this.http.get<User>(`${this.API_URL}/me`)
  }

  getAddress(){
    return this.http.get<Address[]>(`${this.API_URL}/address`);
  }

  updateUser(id: number,username: string,role: number) {
    return this.http.put(`${this.API_URL}/user/${id}`,{username: username, role: role}, this.optionRequete);
  }

  deleteAddress(id: number) {
    return this.http.delete(`${this.API_URL}/address/${id}`);
  }

  addAddress(address: Address) {
    return this.http.post(`${this.API_URL}/address`, address);
  }

  updateAddress(address: Address) {
    return this.http.put(`${this.API_URL}/address`, address);
  }

  getAllUsers() {
    return this.http.get<User[]>(`${this.API_URL}/user`);
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.API_URL}/user/${id}`,this.optionRequete);
  }
}
