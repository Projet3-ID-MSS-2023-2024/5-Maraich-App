import { Component } from '@angular/core';
import { EditorModule } from 'primeng/editor';
import {FormsModule} from "@angular/forms";
import { ButtonModule } from 'primeng/button';
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
@Component({
  selector: 'app-add-request',
  standalone: true,
  imports: [EditorModule, FormsModule, ButtonModule],
  templateUrl: './add-request.component.html',
  styleUrl: './add-request.component.css'
})
export class AddRequestComponent {
  requestBody: string = ""
  user!: User;
  newRequest : Requests = {idRequest:0, requestBody: '', user : this.user};

  constructor(private requestService: RequestService) {}

  onSubmit() {
    this.newRequest.requestBody=this.requestBody;
    this.requestService.addRequest(this.newRequest).subscribe({
      next: (response) => {
          console.log('Succès', response);
        },
      error: (error) => {
          console.error('Error :', error)
        }
      }
    );
  }
}
