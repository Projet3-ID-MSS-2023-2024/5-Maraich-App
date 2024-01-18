import {Component, OnInit} from '@angular/core';
import {Order} from "../../../../models/order";
import {ActivatedRoute, Router} from "@angular/router";
import {OrderService} from "../../../../services/order.service";
import {TableModule} from "primeng/table";
import {ButtonModule} from "primeng/button";
import {DataViewModule} from "primeng/dataview";
import {InputNumberModule} from "primeng/inputnumber";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {ImageService} from "../../../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-client-order-view',
  standalone: true,
  imports: [
    TableModule,
    ButtonModule,
    DataViewModule,
    InputNumberModule,
    NgForOf,
    NgIf,
    NgClass,
  ],
  templateUrl: './client-order-view.component.html',
  styleUrl: './client-order-view.component.css'
})
export class ClientOrderViewComponent implements OnInit{

  idOrder: number = -1;
  order!: Order;

  constructor(private orderService: OrderService, private route: ActivatedRoute, private router: Router, private imageService: ImageService, private sanitizer:DomSanitizer) {
    this.route.paramMap.subscribe(params =>{
      this.idOrder = Number(params.get('id'));
    });
    this.orderService.getOrderById(this.idOrder).subscribe({
      next: response => {
        this.order = response;
        // console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    });
  }

  ngOnInit() {
    this.loadImages()
  }

  toList() {
    this.router.navigate(['/commande/client/liste']);
  }

  loadImages(): void {
    const loadImagePromises = this.order.orderProducts.map((r) => {
      const fileName = r.product.picturePath;
      return new Promise<void>((resolve) => {
        this.imageService.getImage(fileName).subscribe({
          next: (data: Blob) => {
            r.product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
            resolve();
          },
          error: () => {
            resolve();
          },
        });
      });
    });

    // Ensure that all images are loaded before calling updateFilteredProducts
    Promise.all(loadImagePromises).then(() => {
    });
  }
}
