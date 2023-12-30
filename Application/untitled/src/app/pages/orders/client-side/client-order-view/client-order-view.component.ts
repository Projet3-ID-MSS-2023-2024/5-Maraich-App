import { Component } from '@angular/core';
import {Order} from "../../../../models/order";
import {ActivatedRoute, RouterLink} from "@angular/router";
import {OrderService} from "../../../../services/order.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-client-order-view',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './client-order-view.component.html',
  styleUrl: './client-order-view.component.css'
})
export class ClientOrderViewComponent {

  idOrder: number = -1;
  order!: Order;

  constructor(private orderService: OrderService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe(params =>{
      this.idOrder = Number(params.get('id'));
    });
    this.orderService.getOrderById(this.idOrder).subscribe({
      next: response => {
        this.order = response;
        console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    });
  }
}
