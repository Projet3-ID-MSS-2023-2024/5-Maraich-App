import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {ChipsModule} from "primeng/chips";
import {FormsModule} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {Router, RouterLink} from "@angular/router";
import {PasswordModule} from "primeng/password";
import {CookieService} from "ngx-cookie-service";
import {RankEnum} from "../../../models/rankEnum";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, CheckboxModule, ButtonModule, RippleModule, ChipsModule, FormsModule, RouterLink, PasswordModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  userEmail!: string;
  userPassword!: string;
  errorMessage!: string;

  constructor(private authService: AuthService, private route: Router, private cookieService : CookieService) {
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
          if (response.message != undefined) {
            if (response.message === "The user is not activate and the activation code has expired, a new one has been sent !") {
              this.errorMessage = "Votre compte n'est pas vérifié et votre code d'activation a expiré. Un nouveau a été envoyé dans vos e-mails.";
            } else if (response.message === "The user is not activate, check your mail !") {
              this.errorMessage = "Votre compte n'est pas vérifié. Consultez vos mails";
            } else if (response.message === "Bad credentials") {
              this.errorMessage = 'Échec de connexion : Vérifiez votre mot de passe et/ou votre adresse mail.';
            }
            console.log(response.message);
          } else {
            // Save the token in a cookie
<<<<<<< HEAD:Application/untitled/src/app/components/auth/login/login.component.ts
            document.cookie = `access_token=${response.bearer}`;

            // Navigate to the farmers display TO BE DONE
            this.route.navigate(["/home"]);
=======
            this.cookieService.set("access_token", response.bearer, undefined,  undefined, undefined, true, "Lax");
            this.authService.getRankFromCookie();
            // Navigate to the home page display
            this.route.navigate(["/accueil"]);
>>>>>>> develop:Application/untitled/src/app/pages/auth/login/login.component.ts
          }

        },
        error: (error) => {
          // Handle login errors here
          console.log(error);
        }
      });
    } else {
      if (this.userEmail)
        this.errorMessage = 'Échec de connexion : Veuillez fournir un mot de passe.';
      else
        this.errorMessage = 'Échec de connexion : Veuillez fournir une adresse email.';
    }
  }


}
