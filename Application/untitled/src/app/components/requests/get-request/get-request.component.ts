import { Component } from '@angular/core';
import {ButtonModule} from "primeng/button";
import {EditorModule} from "primeng/editor";
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {OnInit} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-get-request',
  standalone: true,
  imports: [
    ButtonModule,
    EditorModule,
    FormsModule
  ],
  templateUrl: './get-request.component.html',
  styleUrl: './get-request.component.css'
})
export class GetRequestComponent implements OnInit{
  Request : Requests = {idRequest:0, requestBody: ''};
  ref: DynamicDialogRef | undefined;
  constructor(private requestService: RequestService, public dialogService: DialogService) {}

  ngOnInit() {
    //this.getRequest()
  }

  private getRequest(id : number) {
    this.requestService.getRequestById(id).subscribe({
    next: (response) => {
        console.log('Succes : ', response);
        this.Request = response;
      },
    error: (error) => {
      console.log('Error: ', error)
    }
    })
  }

}
