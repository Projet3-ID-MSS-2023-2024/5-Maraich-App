import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {EditorModule} from "primeng/editor";
import {User} from "../../../models/user";
import {Requests} from "../../../models/requests";
import {RequestService} from "../../../services/request.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../../../services/user.service";
import {RankEnum} from "../../../models/rankEnum";
import {DialogModule} from "primeng/dialog";
import {InputTextareaModule} from "primeng/inputtextarea";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-get-request',
  standalone: true,
  imports: [
    ButtonModule,
    EditorModule,
    FormsModule,
    ReactiveFormsModule,
    DialogModule,
    InputTextareaModule,
    NgIf
  ],
  templateUrl: './get-request.component.html',
  styleUrl: './get-request.component.css'
})
export class GetRequestComponent implements OnInit{
  @Input() request! : Requests;
  @Input() user!: User;
  @Input() visible!: boolean;
  @Output() refreshTheList = new EventEmitter<void>();
  constructor(private requestService: RequestService, private userService: UserService) {
  }

  ngOnInit() {
  }

  validateRequest() {
    this.user!.rank = {
      name: RankEnum.MARAICHER,
      idRank:2,
      priorityLevel:2
    };
    this.userService.updateUserAdmin(this.user!).subscribe({
      next: response => {
        this.deleteARequest();
      },
      error: err => {
        console.error("Error : ", err);
      }
    })
  }

  deleteARequest() {
    this.requestService.deleteRequestById(this.request.idRequest).subscribe({
      next: response => {
        this.refreshTheList.emit();
      },
      error: err => {
        console.error("Error : ", err);
      }
    })
  }
}
