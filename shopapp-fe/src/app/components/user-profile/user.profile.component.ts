import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from "@angular/router";
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { TokenService } from '../../services/token.service';
import { UserResponse } from '../../responses/user/user.response';
import { UpdateUserDTO } from '../../dtos/user/update.user.dto';


@Component({
    selector: 'user-profile',
    templateUrl: './user.profile.component.html',
    styleUrls: ['./user.profile.component.scss']
})


export class UserProfileComponent implements OnInit {

    userProfileForm: FormGroup;
    userResponse?: UserResponse;
    token: string = '';

    constructor(
        private router: Router,
        private formBuilder: FormBuilder,
        private userService: UserService,
        private tokenService: TokenService,
        private activatedRoute: ActivatedRoute
    ) {
        // Tạo formGroup và các FormControl 
        this.userProfileForm = this.formBuilder.group({
            fullname: [''],
            // Not recommend
            // email: ['', [Validators.email]],
            // Not recommend
            // phone_number: ['', [Validators.minLength(6)]],
            address: ['', [Validators.minLength(5)]],
            password: ['', [Validators.minLength(3)]],
            retype_password: ['', [Validators.minLength(3)]],
            date_of_birth: [Date.now()],
        },{
            validators: this.passwordMatchValidator
        } 
    )
    }

    ngOnInit(): void {
        debugger
        this.token = this.tokenService.getToken() ?? '';
        this.userService.getUserDetail(this.token).subscribe({
            next: (response: any) => {
                debugger
                this.userResponse = {
                    ...response,
                    date_of_birth: new Date(response.date_of_birth)
                }
                this.userProfileForm.patchValue({
                    fullname: this.userResponse?.fullname ?? '',
                    address: this.userResponse?.address ?? '',
                    date_of_birth: this.userResponse?.date_of_birth.toISOString().substring(0,10),
                })
                this.userService.saveUserResponse(this.userResponse);

            },
            complete: () => {
                debugger
            },
            error: (error: any) => {
                debugger
                console.error('Error fetching user details:', error);
            }
        })
    }

    passwordMatchValidator(): ValidatorFn {
        return (formGroup: AbstractControl): ValidationErrors | null => {
            const password = formGroup.get('password')?.value;
            const retypePassword = formGroup.get('retype_password')?.value;
            if (password !== retypePassword) {
                return { passwordMismatch: true };
            }
            return null;
        }
    }

    save(): void {
        debugger
        if(this.userProfileForm.valid) {
            const updateUserDTO: UpdateUserDTO = {
                fullname: this.userProfileForm.get('fullname')?.value,
                address: this.userProfileForm.get('address')?.value,
                password: this.userProfileForm.get('password')?.value,
                retype_password: this.userProfileForm.get('retype_password')?.value,
                date_of_birth: this.userProfileForm.get('date_of_birth')?.value,
            };
            this.userService.updateUser(this.token, updateUserDTO).subscribe({
                next: (response: any) => {
                    debugger
                    this.userService.removeUserInLocalStorage();
                    this.tokenService.removeToken();
                    this.router.navigate(['/login']);
                },
                error: (error: any) => {
                    debugger
                    console.error('Error updating user profile:', error);
                }

            })
        }else{
            if(this.userProfileForm.hasError('passwordMismatch')) {
                alert('Mật khẩu không khớp!');
            }
        }
    }




}