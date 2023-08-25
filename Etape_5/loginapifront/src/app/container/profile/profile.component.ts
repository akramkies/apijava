import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Address } from 'src/app/model/Address';
import { User } from 'src/app/model/User';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: User =  {id: 0, username: '' , role: 0}
  address: Address[] = []
  users: User[] = []
  newAddress: Address = {street: '', city: '', country: '', postalCode: ''}
  error: string = ''
  delError: string = ''

  constructor(
    private _userService: UserService,
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
          this.address = data;
        }, (error) => {

        }
      )

      this.getAllUsers();
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

  addAddress() {
    this._userService.addAddress(this.newAddress).subscribe(
      () => {
        this.ngOnInit()
      });
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
