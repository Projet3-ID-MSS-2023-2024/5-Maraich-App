import {Component, OnDestroy, OnInit, signal} from '@angular/core';
import {DataViewModule} from "primeng/dataview";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {SharedModule} from "primeng/api";
import {RatingModule} from "primeng/rating";
import {TagModule} from "primeng/tag";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Product} from "../../models/product";
import {ButtonModule} from "primeng/button";
import {ReservationService} from "../../services/reservation.service";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../../services/auth.service";
import {Reservation} from "../../models/reservation";
import {Image} from "primeng/image";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";
import {KnobModule} from "primeng/knob";
import {PaginatorModule} from "primeng/paginator";
import {Subscription} from "rxjs";
import {ProductService} from "../../services/product.service";
import {Shop} from "../../models/shop";
import {Router} from "@angular/router";

@Component({
  selector: 'app-panier',
  standalone: true,
  imports: [
    DataViewModule,
    DropdownModule,
    FormsModule,
    InputTextModule,
    SharedModule,
    RatingModule,
    TagModule,
    NgClass,
    ButtonModule,
    NgForOf,
    NgIf,
    KnobModule,
    PaginatorModule
  ],
  templateUrl: './panier.component.html',
  styleUrl: './panier.component.css'
})
export class PanierComponent implements OnInit, OnDestroy{
  private refreshInterval: any;
  private refreshSubscription!: Subscription;
  reservations! : Reservation[];
  currentShop! : Shop | undefined;
  idUser = 0;
  inputValues: { [key: string]: number } = {};

  constructor(private route : Router, private productService : ProductService, private imageService : ImageService, private sanitizer:DomSanitizer, private reservationService : ReservationService, private authService : AuthService) {
  }

  ngOnInit(): void {
    this.idUser = this.authService.getIdUserFromCookie();
    this.getShoppingCart();
    this.refreshInterval = setInterval(() => {
      this.getShoppingCart();
    }, 5 * 60 * 1000);
  }

  ngOnDestroy(): void {
    // Stop the interval when the composant is destroy
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  }

  private getShoppingCart() {
    this.reservationService.getShoppingCart(this.idUser).subscribe({
      next: (response) => {
        this.reservations = response;
        if(this.reservations[0])
          this.currentShop = this.reservations[0].product.shop;
        this.loadImages()
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  goToShopPage(){
    this.route.navigate(["/shop/" + this.currentShop?.idShop ?? 0]);
  }

  loadImages(): void {
    const loadImagePromises = this.reservations.map((r) => {
      const fileName = r.product.picturePath;
      return new Promise<void>((resolve) => {
        this.imageService.getImage(fileName).subscribe({
          next: (data: Blob) => {
            r.product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
            resolve();
          },
          error: () => {
            resolve();
          },
        });
      });
    });

    // Ensure that all images are loaded before calling updateFilteredProducts
    Promise.all(loadImagePromises).then(() => {
    });
  }

  updateReservation(idReservation: number, reserveQuantity: number){
    if(this.inputValues[idReservation] != null) {
      let newQuantity = reserveQuantity - this.inputValues[idReservation];
      if (newQuantity < 1) {
        this.deleteReservation(idReservation);
      } else {
        this.reservationService.updateReservation(idReservation, newQuantity).subscribe({
          next: (response) => {
            // Update the reserve quantity if the update is ok
            const updatedReservation = this.reservations.find(r => r.idReservation === idReservation);
            if (updatedReservation) {
              updatedReservation.reserveQuantity = newQuantity;
            }
          },
        });
      }
    }
  }

  deleteReservation(idReservation : number){
    this.reservationService.deleteReservation(idReservation).subscribe({
      next: () => {
        const index = this.reservations.findIndex(r => r.idReservation === idReservation);
        if (index !== -1) {
          this.reservations.splice(index, 1);
        }
      },

      error: (error) => {
        console.log(error);
      }
    });
  }

  deleteShoppingCart() {
    this.reservationService.deleteShoppingCartByUserId(this.idUser).subscribe({
      next: () => {
        this.reservations = [];
        this.currentShop = undefined;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

}

