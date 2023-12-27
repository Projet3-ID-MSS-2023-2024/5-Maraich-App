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
import {TagModule} from "primeng/tag";
import {InputTextModule} from "primeng/inputtext";
import {InputNumberModule} from "primeng/inputnumber";
import {DropdownModule} from "primeng/dropdown";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {AddProductComponent} from "../add-product/add-product.component";
import {CookieService} from "ngx-cookie-service";
import {ShopService} from "../../../../services/shop.service";
import {Shop} from "../../../../models/shop";
import {ConfirmationService, MessageService} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";

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
    DropdownModule,
    ConfirmDialogModule,
    ToastModule
  ],
  templateUrl: './list-products.component.html',
  styleUrl: './list-products.component.css',
  providers: [DialogService, ConfirmationService, MessageService]
})
export class ListProductsComponent implements OnInit{
  listProduct: Product[] = [];
  shopId!:number;
  selectedCategory: Category | null = null;
  categories:Category[]=[];
  filteredListProduct: Product[] = [];
  searchTerm: string = '';
  ref: DynamicDialogRef | undefined;
  shop?:Shop;

  constructor(private productService:ProductService,private cookieService:CookieService, private confirmationService:ConfirmationService, private shopService:ShopService,private route:ActivatedRoute,private imageService:ImageService, private categoryService:CategoryService, private sanitizer:DomSanitizer, public dialogService:DialogService, private messageService:MessageService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.shopId = +params['shopId'];

      this.getCategories();
      this.getProducts();
      console.log(this.listProduct[0]);
      this.updateFilteredProducts();
      const jwtToken = this.cookieService.get('access_token');
      this.extractIdUserData(jwtToken);
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
        this.categories = [{ idCategory: 0, nomCategory: 'Tous' }, ...categories];
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

  extractIdUserData(token: string): void {
    if (token) {
      const tokenParts = token.split('.');
      const payload = tokenParts[1];

      // Decode the payload using base64 decoding
      const decodedPayload = atob(payload);

      // Parse the decoded payload as JSON
      const payloadData = JSON.parse(decodedPayload);

      this.shopService.getShopByOwnerId(payloadData.idUser).subscribe({
        next:(shop) => {
          this.shop = shop;
      }, error:(error) => {
        console.log(error);
      }
      })
    }
  }

  updateFilteredProducts(): void {
    this.filteredListProduct = this.listProduct.filter((product) => {
      const categoryMatch = !this.selectedCategory || this.selectedCategory.idCategory === 0 || product.category?.idCategory === this.selectedCategory.idCategory;
      const nameMatch = product.name.toLowerCase().includes(this.searchTerm.toLowerCase());
      return categoryMatch && nameMatch;
    });
  }

  showAddProduct(){
    this.ref = this.dialogService.open(AddProductComponent, {header: "Ajoutez un produit"});

    this.ref.onClose.subscribe((response) => {
      if (response == 'success'){
        this.getProducts();
        this.messageService.add({
          severity:'success',
          summary:'Succès',
          detail:"Le produit a bien été ajouté"
        });
      }
    });
  }

  showEditProduct(product: Product){
    this.ref = this.dialogService.open(AddProductComponent, { header:"Modifiez un produit" , data: { product: product }});

    this.ref.onClose.subscribe((response) => {
      if (response == 'success'){
        this.getProducts();
        this.messageService.add({
          severity:'success',
          summary:'Succès',
          detail:"Le produit a bien été modifié"
        });
      }
    })
  }

  showDeleteProduct(productId: number){
    this.confirmationService.confirm({
      message:'Etes-vous sur de vouloir supprimer ce produit ?',
      accept: () => {
        this.productService.deleteProduct(productId).subscribe({
          next:() => {
            this.getProducts();
            this.messageService.add({
              severity:'success',
              summary:'Succès',
              detail: "Le produit a bien été supprimé"
            })
          },
          error:(error) => {
            console.log(error);
          }
        });
      }
    });
  }
}
