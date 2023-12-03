import {Component, importProvidersFrom, Input, OnInit} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RippleModule} from "primeng/ripple";
import {provideRouter, Router, RouterLink} from "@angular/router";
import {Address} from "../../../models/address";
import {User} from "../../../models/user";
import {PasswordModule} from "primeng/password";
import {AuthService} from "../../../services/auth.service";

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ButtonModule, InputTextModule, ReactiveFormsModule, RippleModule, RouterLink, FormsModule, PasswordModule, NgOptimizedImage],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent{
  user!: User
  passwordConfirmation : string = "";
  firstNameIsNotOk : boolean = false;
  surnameIsNotOk : boolean = false;
  phoneNumberIsNotOk : boolean = false;
  passwordIsNotOk : boolean = false;
  addressRoadIsNotOk : boolean = false;
  addressCityIsNotOk : boolean = false;
  addressPostCodeIsNotOk : boolean = false;
  addressNumberIsNotOk : boolean = false;
  addressEmailIsNotOk : boolean = false;
  passwordFeatIsOk : boolean = true;

  constructor(private authService : AuthService, private route: Router) {
    this.user = {
      idUser: 0, // or any default value
      firstName: "",
      surname: "",
      phoneNumber: "",
      password: "",
      address: { road: "", city: "", postCode: "", number: "" }, // Initialize nested object
      email: "",
      isActif: true,
    };
  }

  fieldsIsValid(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const nameRegex = /^[a-zA-ZÀ-ÿ-]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
    const roadRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
    const postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
    const numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
    const cityRegex = /^[a-zA-Z\s\-.,'()&]+$/;
    const phoneNumberRegex = /^[0-9]+$/;


    this.addressEmailIsNotOk = !emailRegex.test(this.user.email);
    this.firstNameIsNotOk = !nameRegex.test(this.user.firstName);
    this.surnameIsNotOk = !nameRegex.test(this.user.surname);
    this.passwordIsNotOk = !passwordRegex.test(this.user.password);
    this.addressRoadIsNotOk = !roadRegex.test(this.user.address.road);
    this.addressPostCodeIsNotOk = !postCodeRegex.test(this.user.address.postCode);
    this.addressNumberIsNotOk = !numberRegex.test(this.user.address.number);
    this.addressCityIsNotOk = !cityRegex.test(this.user.address.city);
    this.phoneNumberIsNotOk = !phoneNumberRegex.test(this.user.phoneNumber);

    return !(this.addressEmailIsNotOk || this.firstNameIsNotOk || this.surnameIsNotOk || this.passwordIsNotOk || this.addressRoadIsNotOk || this.addressPostCodeIsNotOk
      || this.addressNumberIsNotOk || this.addressCityIsNotOk || this.phoneNumberIsNotOk);

  }

  passwordFeat(): void{
    this.passwordFeatIsOk = this.passwordConfirmation == this.user.password;
  }

  onSubmitForm(){
    console.log(this.user);
    this.passwordFeat()
    if(this.fieldsIsValid() && this.passwordFeatIsOk){
      this.authService.signUp(this.user).subscribe({
          next: (response: any) => {
            this.route.navigate(["/connexion"]);
          }
        }
      );
    }
    else console.log("un soucis");

  }
}
