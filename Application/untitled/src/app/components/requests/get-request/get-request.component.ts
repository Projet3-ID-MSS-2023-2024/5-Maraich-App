import {Component, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {EditorModule} from "primeng/editor";
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {RankEnum} from "../../../models/rankEnum";

@Component({
  selector: 'app-get-request',
  standalone: true,
  imports: [
    ButtonModule,
    EditorModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './get-request.component.html',
  styleUrl: './get-request.component.css'
})
export class GetRequestComponent {
  user!: User;
  request!: Requests;
  constructor(private requestService: RequestService, private userService: UserService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
    this.requestService.getRequestById(this.config.data.id).subscribe({
      next: (response) => {
        console.log('Succes : ', response);
        this.request = response;
      },
      error: (error) => {
        console.error('Error: ', error)
      }
    })
  }

  requestForm = new FormGroup({
    requestBody: new FormControl('')
  })

  validateRequest() {
    this.user.rank.name = RankEnum.MARAICHER;
    this.userService.updateUserRestricted(this.user).subscribe({
      next: response => {
        console.log("Success : ", response);
        this.ref?.close();
        this.updateRequestsList();
      },
      error: err => {
        console.error("Error : ", err);
      }
    })
  }

  declineRequest() {
    this.requestService.deleteRequestById(this.request.idRequest).subscribe({
      next: response => {
        console.log("Success : ", response);
        this.ref?.close();
        this.updateRequestsList();
      },
      error: err => {
        console.error("Error : ", err);
      }
    })
  }

  private updateRequestsList() {
    const refreshRequests = this.config?.data?.refreshRequests;
    if (refreshRequests){
      refreshRequests();
    }
  }
}
