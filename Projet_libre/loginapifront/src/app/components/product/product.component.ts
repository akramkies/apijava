import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CartService } from 'src/app/service/cart/cart.service';
import { map } from 'rxjs/operators';
import { ProductService } from 'src/app/service/product/product.service';
import { Product } from 'src/app/model/Product';


@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {

  product: any;
  cartData: any;
  private productInCart: number = 0;
  loading = false;


  constructor(
    private _cart: CartService,
    private _route: ActivatedRoute,
    private _product: ProductService
    ) {
      this._cart.cartDataObs$.subscribe((cartData) => {
        this.cartData = cartData;
      })
     }

  ngOnInit(): void {
    this.loading = true;
    console.log(this._route.paramMap);
    this._route.paramMap
      .pipe(
        map((param: any) => {
          console.log(param);
          return param.params.id
    })
    ).subscribe((id: string) => {
      this._product.getSingleProduct(id).subscribe((product) => {
        console.log(id);
        this.product = product;
        this.loading = false;
      })
    })
  }


  updateCart(id: string, quantity: number): void {
    this._cart.updateCart(id, quantity);
  }

  addToCart(id: string, quantity: number) {
    this._cart.addProductToCart(id, quantity);
  }

  setProductInCart(count: number) {
    this.productInCart = count;
  }

  getProductInCart(): number {
    return this.productInCart;
  }

}
