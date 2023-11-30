import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RippleModule} from "primeng/ripple";
import {RouterLink} from "@angular/router";
import {Address} from "../../../models/address";

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, ButtonModule, InputTextModule, ReactiveFormsModule, RippleModule, RouterLink, FormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css'
})
export class SignupComponent{

  firstName!: string;
  surname!: string;
  phoneNumber!: string;
  password!: string;
  passwordConfirmation!: string;
  address: Address = { road: '', postCode: '', city: '', number: '' };
  email!: string;

  onSubmitForm(){

  }
}
