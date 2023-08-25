import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Catalog } from 'src/app/model/Catalog';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {
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

  getCatalogs() {
    return this.http.get<Catalog[]>(`${this.API_URL}/catalog`);
  }

  addCatalog(catalog: Catalog) {
    return this.http.post(`${this.API_URL}/catalog`, catalog);
  }

  deleteCatalog(reference: string) {
    return this.http.delete(`${this.API_URL}/catalog/${reference}`, this.optionRequete)
  }
}
