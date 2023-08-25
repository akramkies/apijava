import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  username: string = '';
  password: string = '';
  error: string = ''
  role: number = -1
  constructor(private _authService: AuthService,
    private router: Router) { }

  ngOnInit(): void {
  }

  register() {
    if(this.role != -1)
      this._authService.register(this.username,this.password, this.role).subscribe(
        () => {
          this.router.navigateByUrl("/login")
        }, err => {
          this.error = JSON.parse(err.error.toString())['error']
        }
        )
    else
        this.error = 'Champ `role` obligatoire'
  }
}
