import {Component, OnInit} from '@angular/core';
import {Product} from "../../../../models/product";
import {ProductService} from "../../../../services/product.service";
import {ActivatedRoute} from "@angular/router";
import {CardModule} from "primeng/card";
import {AsyncPipe, CommonModule, NgClass, NgForOf, NgOptimizedImage} from "@angular/common";
import {ImageService} from "../../../../services/image.service";
import {ButtonModule} from "primeng/button";
import {CategoryService} from "../../../../services/category.service";
import {FormsModule} from "@angular/forms";
import {DomSanitizer} from "@angular/platform-browser";
import {Category} from "../../../../models/category";
import {DataViewModule} from "primeng/dataview";
import {RatingModule} from "primeng/rating";
import {TagModule} from "primeng/tag";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {DropdownModule} from "primeng/dropdown";

@Component({
  selector: 'app-list-products',
  standalone: true,
  imports: [
    CardModule,
    NgForOf,
    NgOptimizedImage,
    ButtonModule,
    FormsModule,
    AsyncPipe,
    DataViewModule,
    NgClass,
    TagModule,
    CommonModule,
    InputTextModule,
    InputNumberModule,
    DropdownModule
  ],
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css'
})
export class ListProductsComponent implements OnInit{
  listProduct: Product[] = [];
  shopId!:number;
  selectedCategory: Category | null = null;
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
      this.updateFilteredProducts();
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
    const loadImagePromises = this.listProduct.map((product) => {
      const fileName = product.picturePath;
      return new Promise<void>((resolve) => {
        this.imageService.getImage(fileName).subscribe({
          next: (data: Blob) => {
            product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
            resolve();
          },
          error: () => {
            console.error(`Error loading image ${fileName} from the backend.`);
            resolve();
          },
        });
      });
    });

    // Ensure that all images are loaded before calling updateFilteredProducts
    Promise.all(loadImagePromises).then(() => {
      this.updateFilteredProducts();
    });
  }

  updateFilteredProducts(): void {
    this.filteredListProduct = this.listProduct.filter((product) => {
      const categoryMatch = !this.selectedCategory || product.category?.nomCategory === this.selectedCategory.nomCategory;
      const nameMatch = product.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      return categoryMatch && nameMatch;
    });
  }
}
