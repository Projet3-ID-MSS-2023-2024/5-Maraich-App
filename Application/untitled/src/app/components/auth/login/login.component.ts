import {Component, inject, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CheckboxModule} from "primeng/checkbox";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {ChipsModule} from "primeng/chips";
import {FormsModule} from "@angular/forms";
import {AuthService} from "../../../services/auth.service";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, CheckboxModule, ButtonModule, RippleModule, ChipsModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent{

  userEmail!: string;
  userPassword!: string;

  constructor(private authService : AuthService, private route: Router) {
  }

  onSubmitForm(){
    this.authService.login(this.userEmail, this.userPassword).subscribe({
      next:
        (response: any) => {
          //Enregistrez le token dans un cookie
          document.cookie = `access_token=${response.bearer}`;

          //Renvoyez vers l'affiche des maraichers A FAIRE
           this.route.navigate(["/connexion"]);
        },
      error:
        (error) => {
          //Gérez les erreurs de connexion ici
        }
    }
    );
  }



}
