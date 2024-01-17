import {Component, OnDestroy, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {DataViewModule} from "primeng/dataview";
import {InputNumberModule} from "primeng/inputnumber";
import {NgForOf, NgIf} from "@angular/common";
import {SharedModule} from "primeng/api";
import {Reservation} from "../../models/reservation";
import {Router} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {ImageService} from "../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";
import {ReservationService} from "../../services/reservation.service";
import {AuthService} from "../../services/auth.service";
import {Shop} from "../../models/shop";
import {ICreateOrderRequest, IPayPalConfig, NgxPayPalModule} from "ngx-paypal";

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [
    ButtonModule,
    DataViewModule,
    InputNumberModule,
    NgForOf,
    NgIf,
    SharedModule,
    NgxPayPalModule,
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css'
})
export class OrderComponent implements OnInit, OnDestroy {
  private refreshInterval: any;
  reservations! : Reservation[];
  currentShop! : Shop| undefined;
  idUser = 0;
  paypalConfig?: IPayPalConfig;
  totalPrice: number = 0;

  constructor(private route: Router, private productService: ProductService, private imageService: ImageService, private sanitizer: DomSanitizer, private reservationService: ReservationService, private authService: AuthService) {

  }

  ngOnInit(): void {
    this.idUser = this.authService.getIdUserFromCookie();
    this.getShoppingCart();
    this.refreshInterval = setInterval(() => {
      this.getShoppingCart();
    }, 5 * 60 * 1000);
    this.initConfig();
  }

  ngOnDestroy(): void {
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

  loadImages(): void {
    const loadImagePromises = this.reservations.map((r) => {
      this.totalPrice += r.product.price * r.reserveQuantity;
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
  }

  private initConfig(): void {

    this.paypalConfig = {
      currency: 'EUR',
      clientId: 'AQrmOkeaaxOYvRCl8rHoYNGh0tnGIsMNlZzVhOwmj_kEoW72neGFXxWQpx2GQHYZtKdsrYxKiirwSemo',
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        purchase_units: [
          {
            amount: {
              currency_code: 'EUR',
              value: this.totalPrice.toString(),
              breakdown: {
                item_total: {
                  currency_code: 'EUR',
                  value: this.totalPrice.toString()
                }
              }
            },
            items: [
              {
                name: 'MaraichApp order',
                quantity: '1',
                category: 'DIGITAL_GOODS',
                unit_amount: {
                  currency_code: 'EUR',
                  value: this.totalPrice.toString(),
                },
              }
            ]
          }
        ]
      },
      advanced: {
        commit: 'true'
      },
      style: {
        label: 'paypal',
        layout: 'horizontal',
        shape: 'pill',
        height: 40,
        fundingicons: true,

      },
      onApprove: (data, actions) => {
        console.log('onApprove - transaction was approved, but not authorized', data, actions);
        actions.order.get().then((details: any) => {

          console.log('onApprove - you can get full order details inside onApprove: ', details);
        });
      },
      onClientAuthorization: (data) => {
        console.log('onClientAuthorization - you should probably inform your server about completed transaction at this point', data);
      },
      onCancel: (data, actions) => {
        console.log('OnCancel', data, actions);
      },
      onError: err => {
        console.log('OnError', err);
      },
      onClick: (data, actions) => {
        console.log('onClick', data, actions);
      },
    };
  }

}
