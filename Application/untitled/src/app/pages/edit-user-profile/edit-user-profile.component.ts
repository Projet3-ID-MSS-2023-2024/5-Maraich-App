import {Component, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {PasswordModule} from "primeng/password";
import {RippleModule} from "primeng/ripple";
import {Router, RouterLink} from "@angular/router";
import {User} from "../../models/user";
import {CookieService} from "ngx-cookie-service";
import {UserService} from "../../services/user.service";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {RankEnum} from "../../models/rankEnum";
import {AddRequestComponent} from "../requests/add-request/add-request.component";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-edit-user-profile',
  standalone: true,
  imports: [
    ButtonModule,
    FormsModule,
    InputTextModule,
    NgIf,
    PasswordModule,
    RippleModule,
    RouterLink
  ],
  templateUrl: './edit-user-profile.component.html',
  styleUrl: './edit-user-profile.component.css'
})
export class EditUserProfileComponent implements OnInit{

  constructor(private cookieService: CookieService, private userService : UserService, private route: Router, private ref: DynamicDialogRef, private dialogService: DialogService) {
    this.user = {
      idUser: 0, // or any default value
      firstName: "",
      surname: "",
      phoneNumber: "",
      password: "",
      address: { road: "", city: "", postCode: "", number: "" }, // Initialize nested object
      email: "",
      actif: true,
      orders : [],
      rank : {idRank : 0, name : RankEnum.CUSTOMER, priorityLevel : 0}
    };
  }
  user!: User;
  idUser! : number;
  wantChangePassword : boolean = false;
  passwordConfirmation : string = "";
  firstNameIsNotOk : boolean = false;
  surnameIsNotOk : boolean = false;
  phoneNumberIsNotOk : boolean = false;
  passwordIsNotOk : boolean = false;
  addressRoadIsNotOk : boolean = false;
  addressCityIsNotOk : boolean = false;
  addressPostCodeIsNotOk : boolean = false;
  addressNumberIsNotOk : boolean = false;
  addressEmailIsNotOk : boolean = false;
  addressEmailIsAlreadyUse : boolean = false;
  passwordFeatIsOk : boolean = true;

  ngOnInit(): void {
    this.extractIdUserData();
    this.userService.getUserById(this.idUser).subscribe({
      next: (user) => {
        this.user = user;
        this.passwordConfirmation = this.user.password ?? "";
        if(this.wantChangePassword){
          this.passwordConfirmation = "";
          this.user.password = "";
        }
        },
      error: (error) => {
        console.log(error);
      }
    });
  }

  // Check if all fields are valid using regular expressions
  fieldsIsValid(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const nameRegex = /^[a-zA-ZÀ-ÿ-]+$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;
    const roadRegex = /^[a-zA-Z0-9\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;
    const postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
    const numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
    const cityRegex = /^[a-zA-Z\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;
    const phoneNumberRegex = /^[0-9]+$/;


    this.addressEmailIsNotOk = !emailRegex.test(this.user.email);
    this.firstNameIsNotOk = !nameRegex.test(this.user.firstName);
    this.surnameIsNotOk = !nameRegex.test(this.user.surname);
    this.passwordIsNotOk = !passwordRegex.test(this.user.password ?? "notok");
    this.addressRoadIsNotOk = !roadRegex.test(this.user.address.road);
    this.addressPostCodeIsNotOk = !postCodeRegex.test(this.user.address.postCode);
    this.addressNumberIsNotOk = !numberRegex.test(this.user.address.number);
    this.addressCityIsNotOk = !cityRegex.test(this.user.address.city);
    this.phoneNumberIsNotOk = !phoneNumberRegex.test(this.user.phoneNumber);

    return !(this.addressEmailIsNotOk || this.firstNameIsNotOk || this.surnameIsNotOk || this.passwordIsNotOk || this.addressRoadIsNotOk || this.addressPostCodeIsNotOk
      || this.addressNumberIsNotOk || this.addressCityIsNotOk || this.phoneNumberIsNotOk);

  }

  extractIdUserData() {
    const token = this.cookieService.get('access_token');
    if (token) {
      const tokenParts = token.split('.');
      const payload = tokenParts[1];

      // Decode the payload using base64 decoding
      const decodedPayload = atob(payload);

      // Parse the decoded payload as JSON
      const payloadData = JSON.parse(decodedPayload);
      this.idUser = payloadData.idUser;
    }
  }

  // Check if password and password confirmation match
  passwordFeat(): void{
    this.passwordFeatIsOk = this.passwordConfirmation == this.user.password;
  }

  // Handle form submission
  onSubmitForm(){
    this.addressEmailIsAlreadyUse = false;
    this.passwordFeat()
    if(this.fieldsIsValid() && this.passwordFeatIsOk){
      this.userService.updateUserRestricted(this.user).subscribe({
          next: (response: User) => {
              this.route.navigate(["/accueil"]);
          },
          error: (error) => {
            console.log(error);
          }
        }
      );
    }
  }

  activeForChangePassword(){
    this.wantChangePassword = true;
    this.passwordConfirmation = "";
    this.user.password = "";
  }

  showAdd() {
    this.ref = this.dialogService.open(AddRequestComponent, {
      header: 'Créer une requête',
      data: {ref : this.ref,}
    });
  }
}
