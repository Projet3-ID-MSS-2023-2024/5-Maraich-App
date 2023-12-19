import {Component, OnInit} from '@angular/core';
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {CommonModule} from "@angular/common";
import {Requests} from "../../../models/requests";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {RequestService} from "../../../services/request.service";
import {AddRequestComponent} from "../add-request/add-request.component";
import {DeleteCategoriesComponent} from "../../shop/categories/delete-categories/delete-categories.component";
import {UpdateRequestComponent} from "../update-request/update-request.component";
import {DeleteRequestComponent} from "../delete-request/delete-request.component";

@Component({
  selector: 'app-list-requests',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    CommonModule
  ],
  templateUrl: './list-requests.component.html',
  styleUrl: './list-requests.component.css'
})
export class ListRequestsComponent implements OnInit{
  requests: Requests[] = [];
  ref: DynamicDialogRef | undefined;

  constructor(private requestService: RequestService, public dialogService: DialogService) {}

  ngOnInit() {
    this.refreshRequests();
  }

  private refreshRequests() {
    this.requestService.getRequests().subscribe({
      next: (response) => {
        this.requests = response;
      },
      error: (error) => {
        console.error('Erreur: ', error);
      }
    });
  }

  showAdd() {
    this.ref = this.dialogService.open(AddRequestComponent, {
      header: 'Crée une requête',
      data: {ref : this.ref, refreshRequests: this.refreshRequests.bind(this)}
    });
  }

  showDelete(id: number) {
    this.ref = this.dialogService.open(DeleteRequestComponent, {
      header: 'Supprimer la requête',
      data: {ref: this.ref, id: id, refreshRequests: this.refreshRequests.bind(this)}
    });
  }

  showEdit(id: number) {
    this.ref = this.dialogService.open(UpdateRequestComponent, {
      header: 'Modifiez la requête',
      data: {ref: this, id : id, refreshRequests: this.refreshRequests.bind(this)}
    })
  }
}
