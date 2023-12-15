import { Component } from '@angular/core';
import {CardModule} from "primeng/card";
import {Shop} from "../../models/shop";
import {NgForOf} from "@angular/common";
import {ButtonModule} from "primeng/button";
import {ShopCardComponent} from "../../components/shop/shop-card/shop-card.component";

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    CardModule,
    NgForOf,
    ButtonModule,
    ShopCardComponent
  ],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {

  //shops: Shop[];



}
