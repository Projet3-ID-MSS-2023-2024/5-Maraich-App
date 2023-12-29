import { Component,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {SharedModule} from "primeng/api";
import {ShopService} from "../../../services/shop.service";
import {Shop} from "../../../models/shop";

@Component({
  selector: 'app-shop-card',
  standalone: true,
    imports: [CommonModule, ButtonModule, CardModule, SharedModule],
  templateUrl: './shop-card.component.html',
  styleUrl: './shop-card.component.css'
})
export class ShopCardComponent implements OnInit{

  shops! : Shop[];

  constructor(
    private shopService: ShopService,
  ) { }

  ngOnInit(): void {
    this.getAllShops();
  }

  getAllShops() {
    this.shopService.getAllShops().subscribe(
      (data: Shop[]) => {
        this.shops = data;
      },
      (error: any) => {
        console.error('Error fetching users', error);
      }
    );
  }

}
