import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../../services/category.service";
import {Category} from "../../../models/category";
import {CookieService} from "ngx-cookie-service";
import {Shop} from "../../../models/shop";
import {ShopService} from "../../../services/shop.service";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";
import {Product} from "../../../models/product";
import {CheckboxModule} from "primeng/checkbox";
import {NgIf} from "@angular/common";
import {InputNumberModule} from "primeng/inputnumber";
import {FileUploadModule} from "primeng/fileupload";
import {ProductService} from "../../../services/product.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    InputTextModule,
    ButtonModule,
    CheckboxModule,
    NgIf,
    InputNumberModule,
    FileUploadModule,
    ToastModule
  ],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent implements OnInit{
  idUser: any;
  shop!: Shop;
  category!: Category;
  categories: Category[] =[];
  selectedFile?: File;
  product: Product = {
    id:0, name: "", price:0, description:"", picturePath:"", quantity:0, weight:0, unity:false,category: undefined, shop: undefined
  };
  isEditMode: boolean = false;

  constructor(private productService: ProductService,private shopService: ShopService,private categoryService: CategoryService, private cookieService: CookieService, private ref:DynamicDialogRef, private config:DynamicDialogConfig, private messageService:MessageService) {
  }

  ngOnInit(): void {
    this.getCategories();
    const jwtToken = this.cookieService.get('access_token');
    this.extractIdUserData(jwtToken);
    this.getShopByIdUser();
    if (this.config.data && this.config.data.product){
      this.isEditMode = true;
      this.product = { ...this.config.data.product }
    }else {
      this.isEditMode = false;
    }
  }

  getCategories(){
    this.categoryService.getCategories().subscribe({
      next:(data) => {
        this.categories = data;
      },
      error:(error) => {
        console.error('Error fetching: ', error);
      }
    });
  }

  getShopByIdUser(){
    this.shopService.getShopByOwnerId(this.idUser).subscribe({
      next: (data) => {
        this.shop = data;
        this.product.shop = this.shop;
      },
      error: (error) => {
        console.error('Error fetching: ', error);
      }
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

        this.idUser = payloadData.idUser;
    }
  }

  onFileSelected(event : any){
      const fileInput = event.files && event.files.length > 0 ? event.files[0] : null;

      if(fileInput){
        const maxSizeInBytes = 100000;
        if (fileInput.size < maxSizeInBytes){
            this.selectedFile = event.files[0];
        }
      }
  }

  submit(){
    if (this.product.name && this.product.price > 0 && this.product.description &&  ((this.product.unity && this.product.quantity > 0) || (!this.product.unity && this.product.weight > 0)) && this.product.category){
      if (this.isEditMode){
        if (this.selectedFile) {
          this.productService.postImage(this.selectedFile).subscribe({
            next: (filePath) => {
              this.product.picturePath = filePath.fileName;
              this.submitProduct();
            },
            error: (error) => {
              console.error('Error adding image: ', error);
            }
          })
        }else {
          this.submitProduct();
        }
      }else {
        if (this.selectedFile) {
          this.productService.postImage(this.selectedFile).subscribe({
            next: (filePath) => {
              this.product.picturePath = filePath.fileName;
              this.submitProduct();
            },
            error: (error) => {
              console.error('Error adding image: ', error);
            }
          });
        }
      }
    }else {
      this.messageService.add({
        severity:'error',
        summary:'Erreur',
        detail:"Remplissez tous les champs"
      });
    }
  }

  submitProduct(){
    if (this.isEditMode){
      this.productService.updateProduct(this.product).subscribe({
        next:() => {
          this.ref.close('success');
          },
        error:(error) => {
          console.error('Error updating product: ', error);
        }
      });
    } else {
      this.productService.postProduct(this.product).subscribe({
        next:() => {
          this.ref.close('success');
        },
        error:(error) => {
          console.error('Error adding product: ', error);
        }
      });
    }
  }
}
