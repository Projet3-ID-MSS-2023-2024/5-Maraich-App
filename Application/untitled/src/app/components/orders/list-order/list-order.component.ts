import {Component, OnInit} from '@angular/core';
import {ReorderableRowHandle, TableModule} from "primeng/table";
import {Order} from "../../../models/order";
import {OrderService} from "../../../services/order.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-list-order',
  standalone: true,
  imports: [
    TableModule
  ],
  templateUrl: './list-order.component.html',
  styleUrl: './list-order.component.css'
})
export class ListOrderComponent implements OnInit{

  orders: Order[] = [];

  constructor(private orderService: OrderService, private router: Router) {
  }

  ngOnInit() {
    this.getAllOrders();
  }

  getAllOrders() {
    return this.orderService.getOrders().subscribe({
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
    this.router.navigate(['/order/view', id]);
  }
}
