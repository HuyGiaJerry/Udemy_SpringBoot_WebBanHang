import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../responses/user/user.response';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-header',

  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  userResponse?: UserResponse | null;
  isOpenPopover = false;

  tooglePopover(event: Event): void {
    event.preventDefault();
    this.isOpenPopover = !this.isOpenPopover;
  }

  handleItemClick(index: number): void {
    // case đăng xuất
    if(index === 2){
      this.userService.removeUserInLocalStorage();
      this.tokenService.removeToken();
      this.userResponse = this.userService.getUserResponseInLocalStorage();

    }

    this.isOpenPopover = false; // close the popver
  }

  constructor(
    private userService: UserService,
    private popoverConfig: NgbPopoverConfig,
    private tokenService: TokenService
  ) { }

  ngOnInit(): void {
    this.userResponse = this.userService.getUserResponseInLocalStorage();
    
  }





}
