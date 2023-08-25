import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/service/cart/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  cartData: any;

  constructor(private _cart: CartService) {
    this._cart.cartDataObs$.subscribe((cartData) => {
      this.cartData = cartData;
    })
   }

  ngOnInit(): void {
  }

  getCartTotal(): number { return this._cart.getCartTotal() }

  updateCart(id: string, quantity: number): void {
    this._cart.updateCart(id, quantity);
  }

  removeCartItem(id: string): void {
    this._cart.removeProduct(id);
  }

}
