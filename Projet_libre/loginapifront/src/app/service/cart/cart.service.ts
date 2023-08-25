import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { CartModel, CartModelPublic, CartModelServer } from 'src/app/model/cart';
import { environment } from 'src/environments/environment';
import { OrderService } from '../order/order.service';
import { ProductService } from '../product/product.service';
import { Product } from 'src/app/model/Product';
import { Command } from 'src/app/model/Command';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private API_URL = environment.API_URL;

  private optionRequete = {
    headers: new HttpHeaders({
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json",
    }), responseType: "text" as "json"
  }

  public cartData : CartModel = {
    products: [] as any,
    total: 0
  };

/*   public cartData : CartModel = {
    products: [{
      inCart: 0,
      product: Object.assign({})
    }],
    total: 0
  }; */


  cartDataObs$ = new BehaviorSubject<CartModel>(this.cartData);

  constructor(private http: HttpClient,
              private productService: ProductService,
              private orderService: OrderService,
              private router: Router)
  {

    let cartJson = localStorage.getItem('cart');
    let localCartData = cartJson !== null ? JSON.parse(cartJson) : null ;
    if(localCartData) this.cartData = localCartData;

    this.cartDataObs$.next(this.cartData);

    /* let localCartData = JSON.parse(localStorage.getItem('cart'));
    if(localCartData) this.cartData = localCartData;

    // Get the information from local storage (if any)
    let info: CartModelPublic = JSON.parse(localStorage.getItem('cart'));

    if(info !== null && info !== undefined && info.prodData[0].incart !== 0) {
      // Local storage is not empty and has some informations
      this.cartDataClient = info;

      // Loop through each entry and put it in the cartDataServer object
      this.cartDataClient.prodData.forEach(p => {
        this.productService.getSingleProduct(p.id).subscribe((actualProductInfo: Catalog) => {
          if(this.cartDataServer.data[0].numInCart === 0) {
            this.cartDataServer.data[0].numInCart = p.incart;
            this.cartDataServer.data[0].product = actualProductInfo;

            // 
            this.cartDataClient.total = this.cartDataServer.total;
            localStorage.setItem('cart', JSON.stringify(this.cartDataClient));

          }else {
            // CartDataServer already has some entry in it
            this.cartDataServer.data.push({
              numInCart: p.incart,
              product: actualProductInfo
            });
              // 
              this.cartDataClient.total = this.cartDataServer.total;
              localStorage.setItem('cart', JSON.stringify(this.cartDataClient));
          }
          this.cartData$.next({...this.cartDataServer});
        })
      });
    }*/
    console.log("Cart : ", this.cartData);
  }


  submitCheckout(userId: number, cart: CartModel) {
    return this.http.post<Command>(this.API_URL+'/command',
    {
      user: userId,
      transporter: "Akram",
      notes: this.cartData.total,
      urgent: false,
    },this.optionRequete);
  }
  addProductToCart(id: string, quantity?: number) {
    this.productService.getSingleProduct(id).subscribe(product => {
      //const {id, title, description, category, image, price, amount } = params;
      //const product = {id, title, description, category, image, price, amount};
      if(!this.isProductInCart(id)) {
        quantity = quantity ? quantity : 1;
        this.cartData.products.push({
          product,
          inCart: quantity
        });
      }else {
        let productIndex = this.cartData.products.findIndex(obj => obj.product.reference === id);
        quantity = quantity ? quantity : 1;
        this.cartData.products[productIndex].inCart += quantity;
      }
      this.cartData.total = this.getCartTotal();
      this.cartDataObs$.next({...this.cartData});
      localStorage.setItem('cart', JSON.stringify(this.cartData));
    })
  }

  updateCart(id: string, quantity: number): void {
    if(quantity === 0) {
      this.removeProduct(id);
      return;
    }
    let productIndex = this.cartData.products.findIndex(obj => obj.product.reference === id);
    this.cartData.products[productIndex].inCart = quantity;

    this.cartData.total = this.getCartTotal();
    this.cartDataObs$.next({...this.cartData});
    localStorage.setItem('cart', JSON.stringify(this.cartData));
  }

  removeProduct(id: string): void {
    let updatedProducts: any = this.cartData.products.filter(obj => obj.product.reference !== id);
    this.cartData.products = updatedProducts;
    this.cartData.total = this.getCartTotal();
    this.cartDataObs$.next({...this.cartData});
    localStorage.setItem('cart', JSON.stringify(this.cartData));
  }


  clearCart(): void {
    this.cartData = {
      products: [] as any,
      total: 0
    };
    this.cartDataObs$.next({...this.cartData});
    localStorage.setItem('cart', JSON.stringify(this.cartData));
  }

  getCartTotal(): number {
    let totalSum: number = 0;
    this.cartData.products.forEach(prod => {
      totalSum += (prod.inCart * prod.product.salePrice)
    });
    return totalSum;
  }

  getTotalItems(): number {
    let total: number = 0;
    this.cartData.products.forEach(prod => {
      total += prod.inCart
    });
    return total;
  }

  isProductInCart(id: string): boolean {
    return this.cartData.products.findIndex((obj) => obj.product.reference === id) !== -1;
  }

  getProductInCart(id: string): number {
    if(this.cartData.products.findIndex(obj => obj.product.reference === id) !== -1)
    return this.cartData.products.filter(obj => obj.product.reference === id)[0].inCart;
    return 0;
  }


}


