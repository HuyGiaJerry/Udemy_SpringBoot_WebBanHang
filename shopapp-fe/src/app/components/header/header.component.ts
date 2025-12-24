import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../responses/user/user.response';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../services/token.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',

  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userResponse?: UserResponse | null;
  isOpenPopover = false;

  constructor(
    private userService: UserService,
    private popoverConfig: NgbPopoverConfig,
    private tokenService: TokenService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.userResponse = this.userService.getUserResponseInLocalStorage();

  }

  tooglePopover(event: Event): void {
    event.preventDefault();
    this.isOpenPopover = !this.isOpenPopover;
  }

  handleItemClick(index: number): void {
    // case profile
    if (index === 0) {
      // Xử lý chuyển đến trang hồ sơ người dùng
      this.router.navigate(['/user-profile']);
    }
    // case đăng xuất
    else if (index === 2) {
      this.userService.removeUserInLocalStorage();
      this.tokenService.removeToken();
      this.userResponse = this.userService.getUserResponseInLocalStorage();

    }

    this.isOpenPopover = false; // close the popver
  }







}
