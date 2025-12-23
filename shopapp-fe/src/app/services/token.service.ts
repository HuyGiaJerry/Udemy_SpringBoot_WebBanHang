import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";


@Injectable({
    providedIn: 'root',
})
export class TokenService {
    private readonly TOKEN_KEY = 'access_token';
    private jwtHelper = new JwtHelperService();
    constructor() { }

    getToken(): string | null {
        return localStorage.getItem(this.TOKEN_KEY);
    }

    setToken(token: string): void {
        localStorage.setItem(this.TOKEN_KEY, token);
    }

    removeToken(): void {
        localStorage.removeItem(this.TOKEN_KEY);
    }


    getUserId(): number {
        const token = this.getToken();
        if (!token) return 0;
        const userObj = this.jwtHelper.decodeToken(token);
        if (!userObj || typeof userObj !== 'object') return 0;
        return 'userId' in userObj ? parseInt(userObj['userId']) : 0;
    }

    isTokenExpired(): boolean {
        const token = this.getToken();
        if (!token) {
            return true; // Token không tồn tại, coi như đã hết hạn
        }
        return this.jwtHelper.isTokenExpired(token);
    }

}