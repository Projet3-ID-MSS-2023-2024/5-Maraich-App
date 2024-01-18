import {Component, OnInit} from '@angular/core';
import {CookieService} from "ngx-cookie-service";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";
import {ShopService} from "../../services/shop.service";
import {Shop} from "../../models/shop";
import {ButtonModule} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {PasswordModule} from "primeng/password";
import {RippleModule} from "primeng/ripple";
import {InputTextareaModule} from "primeng/inputtextarea";
import {RankEnum} from "../../models/rankEnum";
import {FileUploadModule} from "primeng/fileupload";
import {ProductService} from "../../services/product.service";
import {TagModule} from "primeng/tag";
import {AdressFormatPipe} from "../../pipe/adress-format.pipe";
import {Image} from "primeng/image";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";
import {SelectButtonModule} from "primeng/selectbutton";
import {ToggleButtonModule} from "primeng/togglebutton";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-edit-shop-profile',
  standalone: true,
  imports: [
    ButtonModule,
    FormsModule,
    InputTextModule,
    NgIf,
    PasswordModule,
    RippleModule,
    InputTextareaModule,
    FileUploadModule,
    TagModule,
    AdressFormatPipe,
    SelectButtonModule,
    ToggleButtonModule
  ],
  templateUrl: './edit-shop-profile.component.html',
  styleUrl: './edit-shop-profile.component.css'
})
export class EditShopProfileComponent implements OnInit{

  idUser!: number;
  wantModify : boolean = false;
  shop! : Shop;
  selectedFile?: File;
  shopIsOkay: boolean = false;
  nameIsNotOk : boolean = false;
  addressRoadIsNotOk : boolean = false;
  addressCityIsNotOk : boolean = false;
  addressPostCodeIsNotOk : boolean = false;
  addressNumberIsNotOk : boolean = false;
  addressEmailIsNotOk : boolean = false;
  addressEmailIsAlreadyUse : boolean = false;
  constructor(private imageService : ImageService, private sanitizer:DomSanitizer, private cookieService: CookieService, private authService : AuthService, private route: Router, private shopService : ShopService, private productService : ProductService) {
  }

  ngOnInit(): void {
    this.idUser = this.authService.getIdUserFromCookie();
    this.shopService.getShopByOwnerId(this.idUser).subscribe({
      next: (shop) => {
        this.shop = shop;
        this.shopIsOkay = this.shop.shopIsOkay;
        if(this.shop.picture)
          this.loadImage();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

// Check if all fields are valid using regular expressions
  fieldsIsValid(): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const roadRegex = /^[a-zA-Z0-9\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;
    const postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
    const numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
    const cityRegex = /^[a-zA-Z\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;

    if(!this.shop.name || this.shop.name.trim() === "")
      this.nameIsNotOk = true;
    this.addressEmailIsNotOk = !emailRegex.test(this.shop.email);
    this.addressRoadIsNotOk = !roadRegex.test(this.shop.address.road);
    this.addressPostCodeIsNotOk = !postCodeRegex.test(this.shop.address.postCode);
    this.addressNumberIsNotOk = !numberRegex.test(this.shop.address.number);
    this.addressCityIsNotOk = !cityRegex.test(this.shop.address.city);

    return !(this.addressEmailIsNotOk || this.addressRoadIsNotOk || this.addressPostCodeIsNotOk
      || this.addressNumberIsNotOk || this.addressCityIsNotOk);

  }


  onSubmitForm() {
    this.nameIsNotOk = false;
    if(this.selectedFile){
      if(this.fieldsIsValid()){
      this.productService.postImage(this.selectedFile).subscribe({
        next: (filePath) => {
          this.shop.picture = filePath.fileName;
          this.shopService.updateShop(this.shop).subscribe({
            next: (response) => {
              this.wantModify = false;
              this.shopIsOkay = true;
              this.loadImage();
            },
            error: (error) => {
              console.error(error);
            }
          });
        },
        error: (error) => {
          console.error('Error adding image: ', error);
        }
      });
    }
    } else if(this.shop.picture) {
      this.shopService.updateShop(this.shop).subscribe({
        next: (response) => {
          this.wantModify = false;
          this.shopIsOkay = true;
          this.loadImage();
        },
        error: (error) => {
          console.error(error);
        }
      });
    }
  }

  onFileSelected(event : any){
    const fileInput = event.files && event.files.length > 0 ? event.files[0] : null;

    if(fileInput){
      const maxSizeInBytes = 100000;
      if (fileInput.size < maxSizeInBytes){
        this.selectedFile = event.files[0];
      }
    }
  }

  loadImage(): void {
    const shop = this.shop; // Remplacez `this.shop` par votre logique pour obtenir la boutique unique

    if (shop) {
      const fileName = shop.picture;
      this.imageService.getImage(fileName).subscribe({
        next: (data: Blob) => {
          shop.pictureUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
        },
        error: () => {
          // Gérer l'erreur si nécessaire
        },
      });
    }
  }

  redirectToShop() {
    this.route.navigate(["shop/" + this.shop.idShop])
  }

  redirectToOrders() {
    this.route.navigate(["maraicher/commande/liste"])
  }

  turnOnOff() {
    this.shopService.turnOnOff(this.shop.idShop).subscribe({
      next: (response : boolean) => {
        this.shop.enable = response;
      },
      error: () => {

      }
    });
  }
}


