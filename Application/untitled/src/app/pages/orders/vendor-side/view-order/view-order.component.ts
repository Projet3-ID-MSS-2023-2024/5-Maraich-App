import {Component} from '@angular/core';
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {compareSegments} from "@angular/compiler-cli/src/ngtsc/sourcemaps/src/segment_marker";
import {resolve} from "@angular/compiler-cli";
import {Order} from "../../../../models/order";
import {OrderService} from "../../../../services/order.service";
import {ButtonModule} from "primeng/button";
import {DataViewModule} from "primeng/dataview";
import {NgClass, NgForOf} from "@angular/common";
import {ImageService} from "../../../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-view-order',
  standalone: true,
  imports: [
    RouterLink,
    ButtonModule,
    DataViewModule,
    NgForOf,
    NgClass
  ],
  templateUrl: './view-order.component.html',
  styleUrl: './view-order.component.css'
})
export class ViewOrderComponent {

  idOrder: number = -1;
  order!: Order;
  constructor(private sanitizer: DomSanitizer, private imageService: ImageService, private orderService: OrderService, private route: ActivatedRoute, private router: Router) {
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

  ReadyOrder() {
    let updatedOrder = this.order;
    updatedOrder.orderIsReady = true;
    this.orderService.updateOrder(updatedOrder).subscribe({
      next: response => {
        // console.log("Success : " ,response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  ArchiveOrder() {
    let updatedOrder = this.order;
    updatedOrder.isArchived = true;
    this.orderService.updateOrder(updatedOrder).subscribe({
      next: response => {
        // console.log("Success : ", response);
      },
      error: error => {
        console.error("Error : ", error);
      }
    })
  }

  toList() {
    this.router.navigate(['/commande/client/liste'])
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
