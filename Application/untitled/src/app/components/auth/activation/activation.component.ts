import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AuthService} from "../../../services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {KeyFilterModule} from "primeng/keyfilter";
import {DialogModule} from "primeng/dialog";

@Component({
  selector: 'app-activation',
  standalone: true,
  imports: [CommonModule, ButtonModule, RippleModule, InputTextModule, ReactiveFormsModule, FormsModule, KeyFilterModule, DialogModule],
  templateUrl: './activation.component.html',
  styleUrl: './activation.component.css'
})
export class ActivationComponent implements OnInit {

  code: string | null = "";
  codeIsNotOk: boolean = false;
  visible: boolean = false;
  errorMessage: string = ""

  constructor(private authService: AuthService, private activedRoute: ActivatedRoute, private route: Router) {
  }

  // Control of the code entered through a regular expression
  codeIsOk(code: string | null): boolean {
    const codeRegex = /^\d{6}$/;
    let codeIsNotOk: boolean = false;
    if (code !== null)
      codeIsNotOk = !codeRegex.test(code);
    return !codeIsNotOk;
  }

  // Handle form submission
  onSubmitCode() {
    this.codeIsNotOk = !this.codeIsOk(this.code)
    if (!this.codeIsNotOk && this.code !== null) {
      this.activateRequest(this.code);
    }
    else {
      this.errorMessage = "Le code entré n'est pas valide.";
      this.visible = true;
    }
  }

  // Make a request to activate the user account
  activateRequest(code: string) {
    this.authService.activate(code).subscribe({
      next: (response: any) => {
        // Navigate based on the response
        if(response.message == "Well done!")
          this.route.navigate(["/connexion"]);
        else if (response.message === "Your validation code is invalid !")
          this.errorMessage = "Le code d'activation entré est invalide ! Il peut ne pas exister, ou une erreur a pu être introduite lors de la saisie.";
        else if (response.message === "Your validation code has expired !")
          this.errorMessage = "Votre code est expiré !";
        else this.errorMessage = response.message;
        this.visible = true;
      },
      error: (error) => {
        console.log(error); }
    });
  }

  ngOnInit(): void {
    // Subscribe to route parameter changes
    this.activedRoute.paramMap.subscribe(
      (params) => {
        let code = params.get("code");
        if (code !== null) {
          if (this.codeIsOk(code)) {
            // Activate the account for a valid code
            this.activateRequest(code);
          } else {
            this.visible = true;
            this.errorMessage = "Le code d'activation entré est invalide ! Il peut ne pas exister, ou une erreur a pu être introduite lors de la saisie.";
          }
        }
      },
    )
  }


}
