import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  username: string = ""
  password: string = ""
  error: string = ""
  constructor(
    private _authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  login() {
    this._authService.login(this.username, this.password).subscribe(
      (data) => {
        localStorage.setItem('UserToken', JSON.parse(data.toString())['token']);
        this.router.navigateByUrl("/profile")
      },err => {
        this.error = JSON.parse(err.error.toString())['token']
      });
  }
}
