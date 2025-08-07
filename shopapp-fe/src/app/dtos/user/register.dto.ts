import {
    IsString,
    IsNotEmpty,
    IsPhoneNumber,
    IsDate
} from 'class-validator'


export class RegisterDTO {
    @IsPhoneNumber()
    phone_number: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    @IsString()
    @IsNotEmpty()
    retype_password: string;

    @IsString()
    fullname: string;

    @IsString()
    address: string;

    @IsDate()
    date_of_birth: Date;
    facebook_id: number;
    google_id: number;
    role_id: number;

    constructor (data: any) {
        this.fullname = data.full_name;
        this.phone_number = data.phone_number;
        this.address = data.address;
        this.password = data.password;
        this.retype_password = data.retype_password;
        this.date_of_birth = data.date_of_birth;
        this.facebook_id = data.facebook_id || 0;
        this.google_id = data.google_id || 0;
        this.role_id = data.role_id || 1; 
    }
    
}