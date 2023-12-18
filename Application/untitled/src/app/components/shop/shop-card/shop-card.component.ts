import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {CardModule} from "primeng/card";
import {SharedModule} from "primeng/api";

@Component({
  selector: 'app-shop-card',
  standalone: true,
    imports: [CommonModule, ButtonModule, CardModule, SharedModule],
  templateUrl: './shop-card.component.html',
  styleUrl: './shop-card.component.css'
})
export class ShopCardComponent {

}
