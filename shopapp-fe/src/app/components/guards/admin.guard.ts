import { inject, Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { UserResponse } from "src/app/responses/user/user.response";
import { TokenService } from "src/app/services/token.service";
import { UserService } from "src/app/services/user.service";


@Injectable({
    providedIn: 'root'
})

export class AdminGuard{
    userResponse?: UserResponse;

    constructor(
        private router: Router,
        private tokenService: TokenService,
        private userService: UserService,
    ){}

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        
        const isTokenExpired = this.tokenService.isTokenExpired();
        const isUser = this.tokenService.getUserId() > 0;
        this.userResponse = this.userService.getUserResponseInLocalStorage();
        const isAdmin = this.userResponse?.role?.name === 'admin';
        debugger
        if(!isTokenExpired && isUser && isAdmin){
            return true;
        }else {
            // Redirect
            this.router.navigate(['/login']);
            return false;
        }
    }
}


export const AdminGuardFn: CanActivateFn = (
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
) : boolean => {
    debugger
    return inject(AdminGuard).canActivate(next, state);
}