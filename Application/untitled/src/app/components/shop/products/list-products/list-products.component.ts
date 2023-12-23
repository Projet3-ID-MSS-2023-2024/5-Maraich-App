import {Component, OnInit} from '@angular/core';
import {Product} from "../../../../models/product";
import {ProductService} from "../../../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {CardModule} from "primeng/card";
import {AsyncPipe, NgForOf, NgOptimizedImage} from "@angular/common";
import {ImageService} from "../../../../services/image.service";
import {ButtonModule} from "primeng/button";
import {Category} from "../../../../models/category";
import {CategoryService} from "../../../../services/category.service";
import {FormsModule} from "@angular/forms";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-list-products',
  standalone: true,
  imports: [
    CardModule,
    NgForOf,
    NgOptimizedImage,
    ButtonModule,
    FormsModule,
    AsyncPipe
  ],
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent implements OnInit{
  listProduct: Product[] = [];
  shopId!:number;
  categories:Category[] = [];
  imageUrls:SafeUrl[] = [];

  constructor(private productService:ProductService, private route:ActivatedRoute,private imageService:ImageService, private categoryService:CategoryService, private sanitizer:DomSanitizer) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.shopId = +params['shopId'];

      this.getCategories();
      this.getProducts();
    });
  }

  getProducts(){
    this.productService.getProductByShop(this.shopId).subscribe({
      next: (products) => {
        this.listProduct = products;
        this.loadImages();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getCategories(){
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  loadImages(): void {
    for (const product of this.listProduct) {
      const fileName = product.picturePath; // Adjust this based on your Product model
      this.imageService.getImage(fileName).subscribe(
        (data: Blob) => {
          const imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
          this.imageUrls.push(imageUrl);
        },
        error => {
          console.error(`Error loading image ${fileName} from the backend.`);
        }
      );
    }
  }
}
