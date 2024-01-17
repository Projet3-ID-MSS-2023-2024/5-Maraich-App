import {Component, OnInit} from '@angular/core';
import {ReorderableRowHandle, TableModule} from "primeng/table";
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";
import {Router} from "@angular/router";
import {AuthService} from "../../../../services/auth.service";
import {UserService} from "../../../../services/user.service";
import {User} from "../../../../models/user";

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

  idUser!: number;
  idShop!: number;
  user!: User;
  orders: Order[] = [];

  constructor(private orderService: OrderService, private router: Router, private authService: AuthService, private userService: UserService) {
    this.idUser = this.authService.getIdUserFromCookie();
    this.userService.getUserById(this.idUser).subscribe({
      next: response => {
        // console.log("Success : ", response);
        this.user=response;
      },
      error: error => {
        console.error("Error : ", error);
      }
    });
    if (this.user.shop != null) {
      this.idShop = this.user.shop.idShop;
    } else {
      alert("Erreur, vous ne possédez pas de magasin. Retour sur l'acceuil");
      this.router.navigate(['/home']);
    }
  }

  ngOnInit() {
    this.getAllOrdersFromShopSeller();
  }

  getAllOrdersFromShopSeller() {
    this.orderService.getOrdersByShopSellerId(this.idShop).subscribe({
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
    this.router.navigate(['/order/view', id]);
  }
}
