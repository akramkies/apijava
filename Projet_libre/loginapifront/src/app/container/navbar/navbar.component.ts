import { Component, Input, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth/auth.service';
import { Observable, Subscription } from 'rxjs';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {


  @Input()
  isLoggedIn: boolean = false;

  @Input()
  username: string = "";

  itemsCount: number = 0;

  constructor(
    private _authService: AuthService,
    //private panierComponent: PanierComponent
  ) { }

  ngOnInit(): void {
    //this.itemsCount = this.panierComponent.loadCart();

  }

  logout() {
    this._authService.logout()
  }

}
