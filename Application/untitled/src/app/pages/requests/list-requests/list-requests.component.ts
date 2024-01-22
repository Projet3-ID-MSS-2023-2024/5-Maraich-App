import {Component, OnInit} from '@angular/core';
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {CommonModule} from "@angular/common";
import {Requests} from "../../../models/requests";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RequestService} from "../../../services/request.service";
import {DeleteRequestComponent} from "../delete-request/delete-request.component";
import {GetRequestComponent} from "../get-request/get-request.component";
import {User} from "../../../models/user";

@Component({
  selector: 'app-list-requests',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    CommonModule,
    GetRequestComponent
  ],
  templateUrl: './list-requests.component.html',
  styleUrl: './list-requests.component.css',
  providers: [DialogService]
})
export class ListRequestsComponent implements OnInit{
  requests: Requests[] = [];
  ref: DynamicDialogRef | undefined;
  requestForDialog! : Requests;
  userForDialog! : User | undefined;
  visible : boolean = false;

  constructor(private requestService: RequestService, public dialogService: DialogService) {}

  ngOnInit() {
    this.refreshRequests();
  }

   refreshRequests() {
    this.visible = false;
    this.requestService.getRequests().subscribe({
      next: (response) => {
        this.requests = response;
        console.log("Succès : ", response);
      },
      error: (error) => {
        console.error('Erreur: ', error);
      }
    });
  }

  showGet(id: number) {
    this.requestService.getRequestById(id).subscribe({
      next: (response) => {
        this.requestForDialog = response;
        this.userForDialog = this.requestForDialog.user;
        this.visible = true;
      },
      error: (error) => {
        console.error('Error: ', error)
      }
    })
  }

  showDelete(id: number) {
    this.ref = this.dialogService.open(DeleteRequestComponent, {
      header: 'Supprimer la requête',
      data: {ref: this.ref, id: id, refreshRequests: this.refreshRequests.bind(this)},
      width: '50%'
    });
  }
}
