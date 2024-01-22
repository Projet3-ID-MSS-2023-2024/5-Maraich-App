import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {SharedModule} from "primeng/api";
import {ShopService} from "../../services/shop.service";
import {Shop} from "../../models/shop";
import {Router} from "@angular/router";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";
import {DataViewModule} from "primeng/dataview";
import {TagModule} from "primeng/tag";
import {AdressFormatPipe} from "../../pipe/adress-format.pipe";
import {ShowFullDescriptionShopComponent} from "../show-full-description-shop/show-full-description-shop.component";

@Component({
  selector: 'app-shop-card',
  standalone: true,
  imports: [CommonModule, ButtonModule, CardModule, SharedModule, DataViewModule, TagModule, AdressFormatPipe, ShowFullDescriptionShopComponent],
  templateUrl: './shop-card.component.html',
  styleUrl: './shop-card.component.css'
})
export class ShopCardComponent implements OnInit{

  shops! : Shop[];
  shop! : Shop;
  visible : boolean = false;

  constructor(
    private shopService: ShopService,
    private route : Router,
    private imageService : ImageService,
    private sanitizer:DomSanitizer
  ) { }

  ngOnInit(): void {
    this.getAllShops();
  }

  getAllShops() {
    this.shopService.getAllShops().subscribe(
      (data: Shop[]) => {
        this.shops = data;
        this.loadImages();
      },
      (error: any) => {
        console.error('Error fetching users', error);
      }
    );
  }

  goToShopPage(idShop : number) {
    this.route.navigate(["/shop/" + idShop]);
  }

  loadImages(): void {
    const loadImagePromises = this.shops.map((s) => {
      const fileName = s.picture;
      return new Promise<void>((resolve) => {
        this.imageService.getImage(fileName).subscribe({
          next: (data: Blob) => {
            s.pictureUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
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

  voirPlus(event: Event, shop : Shop) {
    event.stopPropagation();
    this.shop = shop;
    this.visible = true;
  }

}
