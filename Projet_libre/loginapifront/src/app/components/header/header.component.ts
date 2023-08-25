import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth/auth.service';
import { CartService } from 'src/app/service/cart/cart.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  cartData: any;
  isLoggedIn = false;

  constructor(
    private _cart: CartService,
    private _auth: AuthService,
  ) {
    this._auth.user.subscribe((user) => {
      if(user) this.isLoggedIn = true;
      else this.isLoggedIn = false;
    })
    this._cart.cartDataObs$.subscribe((cartData) => {
      this.cartData = cartData;
    })
   }

  getTotalItems(): number { return this._cart.getTotalItems() }
  getCartTotal(): number { return this._cart.getCartTotal() }


  ngOnInit(): void {
  }

  logout() {
    this._auth.logout();
  }

}
