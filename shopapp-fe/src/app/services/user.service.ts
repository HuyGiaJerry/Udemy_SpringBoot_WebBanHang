import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { environment } from '../environments/environment'; // Adjust the import path as necessary
import { UserResponse } from '../responses/user/user.response';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiRegister = `${environment.apiUrl}/users/register`;
  private apiLogin = `${environment.apiUrl}/users/login`;
  private apiUserDetail = `${environment.apiUrl}/users/details`;

  private apiConfig = {
    headers: this.createHeaders(),
  }
  private createHeaders() {
    return new HttpHeaders(
      {
        'Content-Type': 'application/json',
        'Accept-Language': 'vi',
      })
  }
  constructor(
    private http: HttpClient
  
  ) 
    { }

  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);

  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }

  getUserDetail(token: string){
    return this.http.post(this.apiUserDetail, {}, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      })
    });
  }


  saveUserResponse(userResponse?: UserResponse){
    try {
      if(userResponse == null || !userResponse) return;
      const userResponseJson = JSON.stringify(userResponse);
      localStorage.setItem('user', userResponseJson);
      console.log('User response saved local storage successfully.');
    } catch (error) {
      console.error('Error saving user response:', error);
    }
  }

  getUserResponseInLocalStorage() {
    try {
      const userResponseJson = localStorage.getItem('user');
      if(userResponseJson == null || userResponseJson == undefined) return null;
      const userResponse = JSON.parse(userResponseJson);
      console.log('User response retrieved from local storage successfully.');
      return userResponse ;
    } catch (error) {
      console.error('Error retrieving user response:', error);
      return null;
    }
  }

  removeUserInLocalStorage() : void {
    try {
      localStorage.removeItem('user');
      console.log('User response removed from local storage successfully.');
    } catch (error) {
      console.error('Error removing user response:', error);
    }
  }


}
