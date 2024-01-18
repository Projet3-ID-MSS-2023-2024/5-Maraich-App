import {Component, OnInit} from '@angular/core';
import {TableModule} from "primeng/table";
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";
import {Router} from "@angular/router";
import {AuthService} from "../../../../services/auth.service";
import {DatePipe} from "@angular/common";
import {ShopService} from "../../../../services/shop.service";
import {Shop} from "../../../../models/shop";

@Component({
  selector: 'app-list-order',
  standalone: true,
  imports: [
    TableModule,
    DatePipe
  ],
  templateUrl: './list-order.component.html',
  styleUrl: './list-order.component.css'
})
export class ListOrderComponent implements OnInit{

  idUser!: number;
  idShop!: number;
  shop!: Shop;
  orders: Order[] = [];

  constructor(private orderService: OrderService, private router: Router, private authService: AuthService, private shopService: ShopService) {
  }

  ngOnInit() {
    this.idUser = this.authService.getIdUserFromCookie();
    this.shopService.getShopByOwnerId(this.idUser).subscribe({
      next: response => {
        this.shop = response;
        this.orderService.getOrdersByShopSellerId(this.shop.idShop).subscribe({
          next: response => {
            this.orders = response;
            console.log("Success : ", response);
          },
          error: error => {
            console.error("Error : ", error);
          }
        })
      },
      error: err => {
        console.error('Erreur : ', err);
      }
    })

  }

  viewOrder(id: number) {
    this.router.navigate(['/commande/consulter', id]);
  }
}
