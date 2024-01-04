import {Component} from '@angular/core';
import {ActivatedRoute, RouterLink} from "@angular/router";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {compareSegments} from "@angular/compiler-cli/src/ngtsc/sourcemaps/src/segment_marker";
import {resolve} from "@angular/compiler-cli";
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";

@Component({
  selector: 'app-view-order',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './view-order.component.html',
  styleUrl: './view-order.component.css'
})
export class ViewOrderComponent {

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

  ReadyOrder() {
    let updatedOrder = this.order;
    updatedOrder.orderIsReady = true;
    this.orderService.updateOrder(updatedOrder).subscribe({
      next: response => {
        console.log("Success : " ,response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  ArchiveOrder() {
    let updatedOrder = this.order;
    updatedOrder.isArchived = true;
    this.orderService.updateOrder(updatedOrder).subscribe({
      next: response => {
        console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }
}
