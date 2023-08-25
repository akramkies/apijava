import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { AuthService } from 'src/app/service/auth/auth.service';
import { CartService } from 'src/app/service/cart/cart.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {

  cartData: any;
  currentUser: any;
  currentStep = 1;
  /* cardNumber: string;
  cardName: string;
  cardExpiry: string;
  cardCode: string;
  cartData: any; */
  products: any;
  loading = false;
  successMessage = '';
  orderId = 0;
  email: string = "";
  user: User =  {id: 0, username: '' , role: 0}
  form = {
    email: '',
    country: '',
    city: '',
    streetAddress: '',
    zip: '',
    phone: ''
  }


  constructor(
    private _cart: CartService,
    private _auth: AuthService,
    private _userService: UserService,
    private router: Router
  ) {
    this._auth.user.subscribe((user) => {
      if(user) {
        this.currentUser = user;
        console.log(user);
      }
    })
    this._userService.getUserInfo().subscribe(
      (data) => {
        this.user = data;
        if(data.role.toString() == "ROLE_ADMIN") {
          this.user.role = 1
        }
        else{
          this.user.role = 0
        }
      }
    )
    this._cart.cartDataObs$.subscribe((cartData) => {
      this.cartData = cartData;
    })
  }

  getCartTotal(): number { return this._cart.getCartTotal() }

  ngOnInit(): void {
  }

  submitCheckout() {
    this.loading = true;
    setTimeout(() => {
      this._cart
        .submitCheckout(this.currentUser.user_id, this.cartData)
        .subscribe(
          (res: any) => {
            console.log(res);
            this.loading = false;
            this.orderId = res.numCommand;
            this.currentStep = 4;
            this._cart.clearCart();
            this.router.navigateByUrl("/thankyou")
          },
          (err) => {
            console.log(err);
            this.loading = false;
          }
        );
    }, 750);
  }


}
