import { Component, OnInit } from '@angular/core';
import { Catalog } from 'src/app/model/Catalog';
import { CatalogService } from 'src/app/service/catalog/catalog.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  catalogs: Catalog[] = []

  constructor(
    private _catalogService: CatalogService
  ) { }

  ngOnInit(): void {
    this.getCatalogs();
  }

  getCatalogs() {
    this._catalogService.getCatalogs().subscribe(
      (data) => {
        this.catalogs = data;
      }
    )
  }

}
