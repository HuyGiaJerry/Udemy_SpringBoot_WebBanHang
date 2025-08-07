import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../dtos/user/login.dto';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-login',

  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;
  phoneNumber: string ;
  password: string ;

  constructor(private router: Router, private userService: UserService) { 
    this.phoneNumber = '';
    this.password = '';
  }

  onChangePhoneNumber() {
      console.log(`Phone number changed: ${this.phoneNumber}`);
    }
    
    onLogin() {
      const msg = `phoneNumber: ${this.phoneNumber} ` +
        `password: ${this.password} `;
      // alert(msg);
      debugger
  
      const loginDTO: LoginDTO =
      {
        "phone_number": this.phoneNumber,
        "password": this.password
        
      }
  
      this.userService.login(loginDTO).subscribe({
        next: (response: any) => {
          debugger
          // Đăng nhập thành công 
          // this.router.navigate(['/login']);
        },
        complete: () => {
          debugger
        },
        error: (error: any) => {
          // Xử lý lỗi nếu có
          alert(`Đăng nhập không thành công: ${error.message}`);
          console.error('Login error:', error);
        }
      })
    }
}
