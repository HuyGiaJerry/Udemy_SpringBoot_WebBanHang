import { Component, ViewChild , OnInit} from '@angular/core';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { LoginResponse } from '../../responses/user/login.responses';
import { TokenService } from '../../services/token.service';
import { RoleService } from 'src/app/services/role.service';
import { Role } from '../../models/role';
import { UserResponse } from '../../responses/user/user.response';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {
  @ViewChild('loginForm') loginForm!: NgForm;

  // Login User
  phoneNumber: string = '';
  password: string = '';
  // Login Admin
  roles: Role[] = [];
  selectedRole: number | undefined;
  remember: boolean = true;
  userResponse?: UserResponse;
  showPassword: boolean = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) { }

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

  toggleShowPassword(){
    this.showPassword = !this.showPassword;
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
        const { token } = response;

        if (this.remember) {
          

          this.tokenService.setToken(token);
          debugger
          this.userService.getUserDetail(token).subscribe({
            next: (response: any) => {
              debugger
              this.userResponse = {
                ...response,
                date_of_birth: new Date(response.date_of_birth)
              }
              this.userService.saveUserResponse(this.userResponse);
              if(this.userResponse?.role?.name === 'admin'){
                this.router.navigate(['/admin/']);
              } else if(this.userResponse?.role?.name === 'user'){ 
                this.router.navigate(['/home']);
              }
          
            },    
            complete: () => {
              debugger
            },
            error: (error: any) => {
              // Xử lý lỗi nếu có
              debugger
              alert(error?.error?.message);
              console.error('Get user detail error:', error);
            }

          })
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
