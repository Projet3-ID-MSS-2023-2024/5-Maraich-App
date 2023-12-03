import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
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
export class ActivationComponent implements OnInit{

  code : string | null = "";
  codeIsNotOk : boolean = false;
  visible: boolean = false;
  errorMessage: string = ""

  showDialog() {
    this.visible = true;
  }
  constructor(private authService : AuthService, private activedRoute: ActivatedRoute, private route: Router) {  }

  codeIsOk (code : string | null) : boolean {
    const codeRegex = /^\d{6}$/;
    let codeIsNotOk : boolean = false;
    console.log(code);
    if(code !== null)
      codeIsNotOk = !codeRegex.test(code);
    console.log(codeIsNotOk);
    return !codeIsNotOk;
  }

  onSubmitCode() {
    this.codeIsNotOk = !this.codeIsOk(this.code)
    if(!this.codeIsNotOk && this.code !== null){
      this.activateRequest(this.code);
    }
  }

  activateRequest(code : string) {
        this.authService.activate(code).subscribe({
        next: (response: any) => {
          this.route.navigate(["/connexion"]);
        },
        error: (error) => {
          // Handle login errors here
          console.log("probleme code");
        }
      });
  }

  ngOnInit(): void {
    this.activedRoute.paramMap.subscribe(
      (params) => {
        let code = params.get("code");
        // console.log(this.code);
        console.log(code);
        if(this.codeIsOk(code) && code !== null) {
          this.activateRequest(code);
        }else console.log("mauvais code");
      },

    )
  }


}
