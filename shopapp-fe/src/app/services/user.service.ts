import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/register.dto';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = "http://localhost:8088/api/v1/users/register";
  constructor(private http: HttpClient) { }

  register(registerDTO: RegisterDTO):Observable<any> {
    const headers = { 'Content-Type': 'application/json' };
    return this.http.post(this.apiUrl, registerDTO, { headers });

  }
}
