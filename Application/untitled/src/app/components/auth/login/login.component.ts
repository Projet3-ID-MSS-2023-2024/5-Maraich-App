import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {ChipsModule} from "primeng/chips";
import {FormsModule} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {Router, RouterLink} from "@angular/router";
import {PasswordModule} from "primeng/password";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, CheckboxModule, ButtonModule, RippleModule, ChipsModule, FormsModule, RouterLink, PasswordModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{

  userEmail!: string;
  userPassword!: string;
  errorMessage!: string;

  constructor(private authService : AuthService, private route: Router) {
  }

  onSubmitForm() {
    this.errorMessage = "";
    // Check that userEmail and userPassword are not null
    if (this.userEmail && this.userPassword) {
      // Check if the email adheres to the email regex
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.userEmail)) {
        this.errorMessage = 'Échec de connexion : Veuillez fournir une adresse e-mail valide.'; // Please provide a valid email address.
        return; // Stop execution if the email is not valid
      }

      // Check if the password adheres to the criteria
      const passwordRegex = /^(?=.*[A-Z])(?=.*[0-9]).{8,}$/;
      if (!passwordRegex.test(this.userPassword)) {
        this.errorMessage = 'Échec de connexion : Vérifiez votre mot de passe et/ou votre adresse mail.'; // The password must contain at least one uppercase letter, one digit, and have a minimum length of 8 characters.
        return; // Stop execution if the password does not meet the criteria
      }

      // If everything is okay, call the login method
      this.authService.login(this.userEmail, this.userPassword).subscribe({
        next: (response: any) => {
          // Save the token in a cookie
          document.cookie = `access_token=${response.bearer}`;

          // Navigate to the farmers display TO BE DONE
          this.route.navigate(["/connexion"]);
        },
        error: (error) => {
          // Handle login errors here
        }
      });
    } else {
      if(this.userEmail)
        this.errorMessage = 'Échec de connexion : Veuillez fournir un mot de passe.';
      else
        this.errorMessage = 'Échec de connexion : Veuillez fournir une adresse email.';
    }
  }




}
