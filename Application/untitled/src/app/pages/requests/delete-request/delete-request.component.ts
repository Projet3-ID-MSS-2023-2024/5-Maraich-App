import { Component } from '@angular/core';
import {CommonModule} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {RequestService} from "../../../services/request.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-delete-request',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './delete-request.component.html',
  styleUrl: './delete-request.component.css'
})
export class DeleteRequestComponent {

  constructor(private requestService : RequestService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {}

  deleteRequest() {
    this.requestService.deleteRequestById(this.config?.data.id).subscribe({
      next: (response) => {
       console.log('Deleted: ', response);
       this.ref?.close();
       this.updateRequestsList();
      },
      error: (error) => {
       console.error('Error: ', error);
      }
    }
    )
  }

  private updateRequestsList() {
    const refreshRequests = this.config?.data?.refreshRequests;
    if (refreshRequests){
      refreshRequests();
    }
  }

  close() {
    this.ref.close();
  }

}
