import { Component, ViewChild } from '@angular/core';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { LoginResponse } from '../../responses/user/login.responses';
import { TokenService } from '../../services/token.service';
import { RoleService } from 'src/app/services/role.service';
import { Role } from '../../models/role';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string = '11223344';
  password: string = '11223344';
  roles: Role[] = [];
  selectedRole: number | undefined;
  remember: boolean = true;

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) {}

  ngOnInit() {

    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {

        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0].id : undefined; // Chọn quyền đầu tiên làm mặc định
      },
      error: (error: any) => {

        console.error('Error fetching roles:', error);
      }
    });
  }

  onChangePhoneNumber() {
    console.log(`Phone number changed: ${this.phoneNumber}`);
  }

  onLogin() {
    debugger
    if (!this.selectedRole) {
      alert('Vui lòng chọn quyền đăng nhập');
      return;
    }
    const loginDTO: LoginDTO =
    {
      phone_number: this.phoneNumber,
      password: this.password,
      role_id: this.selectedRole
    }

    this.userService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        debugger
        // Đăng nhập thành công 
        // Lưu token 
        if (this.remember) {
          const { token } = response;
          this.tokenService.setToken(token);
        }

      },
      complete: () => {
        debugger
      },
      error: (error: any) => {
        // Xử lý lỗi nếu có
        debugger
        alert(error?.error?.message);
        console.error('Login error:', error);
      }
    })
  }
}
