import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { TokenService } from '../../services/token.service';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../responses/user/user.response';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.scss'],
})


export class AdminComponent implements OnInit {
    userResponse?: UserResponse;
    adminComponent: string = 'orders'; // default to 'orders' view

    constructor(
        private tokenService: TokenService,
        private router: Router,
        private userService: UserService
    ) { }

    ngOnInit(): void {
        this.userResponse = this.userService.getUserResponseInLocalStorage();
    }

    logout(): void {
        this.userService.removeUserInLocalStorage();
        this.tokenService.removeToken();
        this.userResponse = this.userService.getUserResponseInLocalStorage();
        this.router.navigate(['/login']);
    }

    showAdminComponent(componentName: string): void {
        this.adminComponent = componentName;
    }

}