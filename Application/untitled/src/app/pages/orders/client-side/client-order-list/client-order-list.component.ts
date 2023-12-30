import {Component, OnInit} from '@angular/core';
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";
import {Router} from "@angular/router";
import {TableModule} from "primeng/table";

@Component({
  selector: 'app-client-order-list',
  standalone: true,
  imports: [TableModule],
  templateUrl: './client-order-list.component.html',
  styleUrl: './client-order-list.component.css'
})
export class ClientOrderListComponent implements OnInit{

  orders: Order[] = [];

  constructor(private orderService: OrderService, private router: Router) {
  }

  ngOnInit() {
    this.getAllOrdersFromUser();
  }

  private getAllOrdersFromUser() {
    this.orderService.getOrders().subscribe({
      next: response => {
        this.orders = response;
        console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  viewOrder(id: number) {
    this.router.navigate(['/order/client/view', id]);
  }
}
