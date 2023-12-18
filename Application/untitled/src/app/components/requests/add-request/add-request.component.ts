import { Component } from '@angular/core';
import { EditorModule } from 'primeng/editor';
import {FormsModule} from "@angular/forms";
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
@Component({
  selector: 'app-add-request',
  standalone: true,
  imports: [EditorModule, FormsModule],
  templateUrl: './add-request.component.html',
  styleUrl: './add-request.component.css'
})
export class AddRequestComponent {
  requestBody: string = ""
  user!: User;
  newRequest : Requests = {idRequest:0, requestBody: '', user : this.user};

  constructor() {
    private
  }
}
