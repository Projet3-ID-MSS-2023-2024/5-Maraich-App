import {Component, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {EditorModule} from "primeng/editor";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {PaginatorModule} from "primeng/paginator";
import {CommonModule} from "@angular/common";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-update-request',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    EditorModule,
    FormsModule,
    PaginatorModule,
    ReactiveFormsModule
  ],
  templateUrl: './update-request.component.html',
  styleUrl: './update-request.component.css'
})
export class UpdateRequestComponent{
  requestBody: string = '';
  modifiedRequest : Requests = {idRequest:0, requestBody: ''};

  constructor(private requestService: RequestService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
    this.requestService.getRequestById(this.config.data.id).subscribe({
      next: (response) => {
        this.modifiedRequest = response;
        this.requestBody = this.modifiedRequest.requestBody;
        // console.log('Succès: ', this.modifiedRequest);
      },
      error: (error) => {
        console.error('Erreur: ', error);
      }
    })
  }

  requestForm = new FormGroup({
    requestBody: new FormControl('', [Validators.required])
  })

  onSubmit() {
    this.modifiedRequest.requestBody=this.requestBody;
    this.requestService.updateRequest(this.modifiedRequest).subscribe({
      next: (response) => {
          // console.log('Succès: ', response);
          this.ref?.close();
          this.updateRequestsList();
        },
      error: (error) => {
          console.error('Error: ', error);
        }
    });
  }

  private updateRequestsList() {
    const refreshRequests = this.config?.data?.refreshRequests;
    if (refreshRequests){
      refreshRequests();
    }
  }

}
