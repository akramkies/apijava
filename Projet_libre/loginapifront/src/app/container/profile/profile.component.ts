import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Address } from 'src/app/model/Address';
import { Catalog } from 'src/app/model/Catalog';
import { User } from 'src/app/model/User';
import { CatalogService } from 'src/app/service/catalog/catalog.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: User =  {id: 0, username: '' , role: 0}
  nbUser: Number = 0;
  nbAddress: Number = 0;
  nbCatalog: Number = 0;
  address: Address[] = []
  users: User[] = []
  catalogs: Catalog[] = []
  newAddress: Address = {street: '', city: '', country: '', postalCode: ''}
  newCatalog: Catalog = {reference: '', family: '', designation: '', costPrice: 0, salePrice: 0, amount: 0, imgUrl: ''}
  error: string = ''
  delError: string = ''

  constructor(
    private _userService: UserService,
    private _catalogService: CatalogService,
    private router: Router
  ) { }

  ngOnInit(): void {
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

      this._userService.getAddress().subscribe(
        (data) => {
          this.nbAddress = data.length;
          this.address = data;
        }, (error) => {

        }
      )

      this.getAllUsers();
      this.getCatalogs();
  }

  getCatalogs() {
    this._catalogService.getCatalogs().subscribe(
      (data) => {
        this.nbCatalog = data.length
        this.catalogs = data;
      }
    )
  }

  updateUser() {
    this._userService.updateUser(this.address[0].userID || 0,this.user.username,this.user.role).subscribe(
      () => {
        localStorage.removeItem("UserToken");
        this.router.navigateByUrl("/login")
      }, () => {
        this.error = 'Username already taken !'
      }
    );
  }

  deleteAddress(index: number) {
    this._userService.deleteAddress(this.address[index].id || 0).subscribe(
      () => {
        this.ngOnInit()
      }
    );
  }

  deleteCatalog(index: number) {
    this._catalogService.deleteCatalog(this.catalogs[index].reference || '').subscribe(
      () => {
        this.ngOnInit()
      }
    );
  }

  addAddress() {
    this._userService.addAddress(this.newAddress).subscribe(
      () => {
        this.ngOnInit()
      });
  }

  addCatalog() {
    this._catalogService.addCatalog(this.newCatalog).subscribe(
      () => {
        this.ngOnInit()
      }
    );
  }

  updateAddress(index: number) {
    this._userService.updateAddress(this.address[index]).subscribe(
      () => {
        this.router.navigateByUrl("/profile")
      });
  }

  getAllUsers() {
    this._userService.getAllUsers().subscribe(
      (data) => {
        this.users = data;
        this.nbUser = data.length
        this.users = this.users.filter(
          data => data.username != this.user.username
        );
        this.router.navigateByUrl('/profile')
      }
    )
  }

  deleteUser(id: number) {
    this._userService.deleteUser(id).subscribe(
      () => {
        this.ngOnInit()
      }, (err) => {
        this.delError = err.statusText
      }
    );
  }
}
