import {Component, OnInit} from '@angular/core';
import { EditorModule } from 'primeng/editor';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import { ButtonModule } from 'primeng/button';
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {AuthService} from "../../../services/auth.service";
import {UserService} from "../../../services/user.service";
@Component({
  selector: 'app-add-request',
  standalone: true,
  imports: [EditorModule, FormsModule, ButtonModule, ReactiveFormsModule],
  templateUrl: './add-request.component.html',
  styleUrl: './add-request.component.css'
})
export class AddRequestComponent {
  idUser!: number;
  newRequest: Requests = {
    idRequest: -1,
    requestBody: ""
  };
  user!: User;

  constructor(private requestService: RequestService, private ref: DynamicDialogRef, private authService: AuthService, private userService: UserService) {
  }


  requestForm = new FormGroup({
    requestBody: new FormControl('',[Validators.required])
  })

  onSubmit() {
    this.idUser = this.authService.getIdUserFromCookie();
    console.log("IdUser : ", this.idUser)
    this.userService.getUserById(this.idUser).subscribe({
      next: (response: User) => {
        this.newRequest.user = response;
        console.log("User inside : ", this.newRequest.user);
        this.newRequest.requestBody=this.requestForm.value.requestBody!;
        this.requestService.addRequest(this.newRequest).subscribe({
            next: (response) => {
              console.log('Succès', response);
              this.ref?.close();
            },
            error: (error) => {
              console.error('Error :', error)
            }
          }
        );
      },
      error: err => {
        console.error('Erreur : ', err);
      }
    });
  }
}
