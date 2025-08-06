import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { RegisterDTO } from '../dtos/register.dto';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  // Khai báo các biến ctuowng ứng với các trường trong form đăng ký
  @ViewChild('registerForm') registerForm !: NgForm;
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(private router: Router, private userService: UserService) {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18); // Giả sử ngày sinh mặc định là 18 năm trước
  }

  onPhoneChange() {
    console.log(`Phone number changed: ${this.phone}`);
  }
  // Match pass and retype pass
  checkPasswordMatch() {
    if (this.password !== this.retypePassword) {
      this.registerForm.form.controls['retypePassword'].setErrors({ 'passwordMismatch': true });
    }
    else {
      this.registerForm.form.controls['retypePassword'].setErrors(null);
    }
  }

  checkAge() {
    if (this.dateOfBirth) {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      if (age < 18) {
        this.registerForm.form.controls['dateOfBirth'].setErrors({ 'invalidAge': true });
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }

  onRegister() {
    const msg = `phone: ${this.phone} ` +
      `password: ${this.password} ` +
      `retypePassword: ${this.retypePassword} ` +
      `fullName: ${this.fullName} ` +
      `address: ${this.address}` +
      `isAccepted: ${this.isAccepted} ` +
      `dateOfBirth: ${this.dateOfBirth}`;
    // alert(msg);
    debugger

    const registerDTO: RegisterDTO =
    {
      "fullname": this.fullName,
      "phone_number": this.phone,
      "address": this.address,
      "password": this.password,
      "retype_password": this.retypePassword,
      "date_of_birth": this.dateOfBirth,
      "facebook_id": 0,
      "google_id": 0,
      "role_id": 1
    }

    this.userService.register(registerDTO).subscribe({
      next: (response: any) => {
        debugger
        // Đăng ký thành công , chuyển qua màn hình đăng nhập
        this.router.navigate(['/login']);
      },
      complete: () => {
        debugger
      },
      error: (error: any) => {
        // Xử lý lỗi nếu có
        alert(`Đăng ký không thành công: ${error.error.message}`);
      }
    })
  }

}
