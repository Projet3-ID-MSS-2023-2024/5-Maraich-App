import { Component, Input } from '@angular/core';
import { ShopService } from './shop.service';
import { Shop } from './Shop';

@Component({
  selector: 'app-card-list',
  templateUrl: './card-list.component.html',
  styleUrls: [
    './card-list.component.css'
  ]
})
export class CardListComponent {

  shops: Shop[] = [];

  constructor(private shopService: ShopService) {}

  ngOnInit(): void {
    this.shopService.getShops().subscribe(
      (data) => {
        this.shops = data;
      },
      (error) => {
        console.error('Error fetching shops', error);
      }
    );
  }
}
