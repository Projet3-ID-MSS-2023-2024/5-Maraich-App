import {Component, OnInit} from '@angular/core';
import {Product} from "../../../../models/product";
import {ProductService} from "../../../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {CardModule} from "primeng/card";
import {AsyncPipe, NgForOf, NgOptimizedImage} from "@angular/common";
import {ImageService} from "../../../../services/image.service";
import {ButtonModule} from "primeng/button";
import {CategoryService} from "../../../../services/category.service";
import {FormsModule} from "@angular/forms";
import {DomSanitizer} from "@angular/platform-browser";
import {Category} from "../../../../models/category";

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
  selectedCategory: string | undefined;
  categories:Category[]=[];
  filteredListProduct: Product[] = [];
  searchTerm: string = '';

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
      this.imageService.getImage(fileName).subscribe({
        next:(data:Blob) => {
          product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
        },
        error:() => {
          console.error(`Error loading image ${fileName} from the backend.`);

        }
      });
    }
  }

  updateFilteredProducts(): void {
    this.filteredListProduct = this.listProduct.filter((product) =>
      (this.selectedCategory ? product.category?.nomCategory === this.selectedCategory : true) &&
      (product.name.toLowerCase().includes(this.searchTerm.toLowerCase()))
    );
  }
}
