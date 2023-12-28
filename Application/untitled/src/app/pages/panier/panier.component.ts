import {Component, OnInit} from '@angular/core';
import {DataViewModule} from "primeng/dataview";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {SharedModule} from "primeng/api";
import {RatingModule} from "primeng/rating";
import {TagModule} from "primeng/tag";
import {NgClass, NgForOf} from "@angular/common";
import {Product} from "../../models/product";
import {ButtonModule} from "primeng/button";
import {ReservationService} from "../../services/reservation.service";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../../services/auth.service";
import {Reservation} from "../../models/reservation";
import {Image} from "primeng/image";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";

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
    NgForOf
  ],
  templateUrl: './panier.component.html',
  styleUrl: './panier.component.css'
})
export class PanierComponent implements OnInit{
  reservations! : Reservation[];
  idUser = 0;

  constructor(private imageService : ImageService, private sanitizer:DomSanitizer, private reservationService : ReservationService, private authService : AuthService) {
  }

  ngOnInit(): void {
    this.idUser = this.authService.getIdUserFromCookie();
    this.reservationService.getShoppingCart(this.idUser).subscribe({
      next: (response) => {
        this.reservations = response;
        this.loadImages()
      },
      error: (error) => {
        console.log(error);
      }
    });
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
}
