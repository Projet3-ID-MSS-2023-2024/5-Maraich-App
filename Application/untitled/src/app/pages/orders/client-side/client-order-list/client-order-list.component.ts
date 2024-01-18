import {Component, OnInit} from '@angular/core';
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";
import {Router} from "@angular/router";
import {TableModule} from "primeng/table";
import {AuthService} from "../../../../services/auth.service";
import {UserService} from "../../../../services/user.service";
import {User} from "../../../../models/user";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-client-order-list',
  standalone: true,
  imports: [TableModule, DatePipe],
  templateUrl: './client-order-list.component.html',
  styleUrl: './client-order-list.component.css'
})
export class ClientOrderListComponent implements OnInit{

  idUser!: number;
  user!: User;
  orders: Order[] = [];

  constructor(private orderService: OrderService, private router: Router, private authService: AuthService, private userService: UserService) {
    this.idUser = this.authService.getIdUserFromCookie();
    this.userService.getUserById(this.idUser).subscribe({
      next: response => {
        this.user = response;
        // console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  ngOnInit() {
    this.getAllOrdersFromUser();
  }

  private getAllOrdersFromUser() {
    this.orderService.getOrders().subscribe({
      next: response => {
        this.orders = response;
        // console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  viewOrder(id: number) {

    this.router.navigate(['/commande/client/consulter', id]);
  }
}
