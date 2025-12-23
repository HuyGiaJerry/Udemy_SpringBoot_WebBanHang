import { Injectable, inject } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateFn, Router, UrlTree } from '@angular/router';
import { TokenService } from 'src/app/services/token.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard {
    constructor(private tokenService: TokenService, private router: Router) { }

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
        const isTokenExpired = this.tokenService.isTokenExpired();
        const isUserIdValid = this.tokenService.getUserId() > 0;
        console.log('AuthGuard: isTokenExpired =', isTokenExpired, ', isUserIdValid =', isUserIdValid);
        if (!isTokenExpired && isUserIdValid) {
            return true;
        } else {
            // Trả về UrlTree để Angular Router tự redirect về /login
            return this.router.parseUrl('/login');
        }
    }
}

// Functional guard phải trả về boolean | UrlTree
export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree => {
    return inject(AuthGuard).canActivate(next, state);
};