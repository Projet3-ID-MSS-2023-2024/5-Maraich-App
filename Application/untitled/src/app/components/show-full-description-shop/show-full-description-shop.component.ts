import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DialogModule} from "primeng/dialog";
import {NgIf} from "@angular/common";
import {SharedModule} from "primeng/api";
import {Shop} from "../../models/shop";

@Component({
  selector: 'app-show-full-description-shop',
  standalone: true,
    imports: [
        DialogModule,
        NgIf,
        SharedModule
    ],
  templateUrl: './show-full-description-shop.component.html',
  styleUrl: './show-full-description-shop.component.css'
})
export class ShowFullDescriptionShopComponent {
  @Input() visible!: boolean;
  @Input() shop! : Shop;
  @Output() cancelBack = new EventEmitter<void>();
}
