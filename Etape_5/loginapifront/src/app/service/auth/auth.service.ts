import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_URL = environment.API_URL;

  private optionRequete = {
    headers: new HttpHeaders({
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    }), responseType: "text" as "json"
  }

  public user: Observable<string>
  public userSubject: BehaviorSubject<string>
  constructor(private http: HttpClient, private router: Router) {
      this.userSubject = new BehaviorSubject<string>(localStorage.getItem("UserToken") || "");
      this.user = this.userSubject.asObservable();
  }

  login(username: string, password: string) {
    return this.http.post(`${this.API_URL}/authenticate`, {username: username, password: password}, this.optionRequete);
  }

  register(username: string, password: string, role: number) {
    return this.http.post(`${this.API_URL}/register`, {username: username, password: password, role: role}, this.optionRequete);
  }

  logout(){
    localStorage.removeItem("UserToken");
    this.userSubject.next("");
    this.router.navigateByUrl("/login")
  }
}