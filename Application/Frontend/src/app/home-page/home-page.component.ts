import { Component } from '@angular/core';
import { Shop } from '../model/shop';
import { ShopService } from '../service/shop.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent {

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
