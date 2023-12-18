import {Component, OnInit} from '@angular/core';
import {CategoryService} from "../../../../services/category.service";
import {Category} from "../../../../models/category";
import {CookieService} from "ngx-cookie-service";
import {Shop} from "../../../../models/shop";
import {ShopService} from "../../../../services/shop.service";
import {DropdownModule} from "primeng/dropdown";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";
import {Product} from "../../../../models/product";
import {CheckboxModule} from "primeng/checkbox";
import {NgIf} from "@angular/common";
import {InputNumberModule} from "primeng/inputnumber";
import {FileUploadModule} from "primeng/fileupload";
import {ProductService} from "../../../../services/product.service";

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
    FileUploadModule
  ],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent implements OnInit{
  idUser: any;
  shop!: Shop;
  categories: Category[] =[];
  selectedFile?: File;
  product: Product = {
    id:0, name: "", price:0, description:"", picturePath:"", quantity:0, weight:0, isUnity:false,category: undefined, shop: undefined
  };

  constructor(private productService: ProductService,private shopService: ShopService,private categoryService: CategoryService, private cookieService: CookieService) {
  }

  ngOnInit(): void {
    this.getCategories();
    const jwtToken = this.cookieService.get('access_token');
    this.extractIdUserData(jwtToken);
    this.getShopByIdUser();
  }

  getCategories(){
    this.categoryService.getCategories().subscribe(
      (data) =>{
        this.categories = data;
      },
        (error) => {
          console.error('Error fetching: ', error);
        }
    );
  }

  getShopByIdUser(){
    this.shopService.getShopByOwnerId(this.idUser).subscribe(
        (data) => {
          this.shop = data;
        },
        (error)=>{
          console.error('Error fetching:', error);
        }
    )
  }

  extractIdUserData(token: string): void {
    if (token) {
      const tokenParts = token.split('.');
      const payload = tokenParts[1];

      // Decode the payload using base64 decoding
        const decodedPayload = atob(payload);

        // Parse the decoded payload as JSON
        const payloadData = JSON.parse(decodedPayload);

        this.idUser = payloadData.idIUser;
    }
  }

  onFileSelected(event : any){
      const fileInput = event.files && event.files.length > 0 ? event.files[0] : null;

      if(fileInput){
        const maxSizeInBytes = 100000;
        if (fileInput.size < maxSizeInBytes){
            this.selectedFile = event.files[0];
            console.log(this.selectedFile);
        }
      }
  }

  submit(){
    if (this.selectedFile) {
      this.productService.postProduct(this.product, this.selectedFile).subscribe(
        (response) => {
          console.log('Product added successfully: ', response);
        },
        (error) => {
          console.error('Error adding product: ', error);
        }
      );
    }

      /*
    if (
      !this.product.name || this.product.name.trim() === '' ||
      !this.product.price || this.product.price.toString().trim() === ''||
      !this.product.description || this.product.description.trim() === '' ||
      !this.product.category
    )
    {
        if (this.product.isUnity){
            this.product.weight = 0;
        }else {
            this.product.quantity = 0;
        }

        if(this.selectedFile){
          console.log(this.product);
         this.productService.postProduct(this.product, this.selectedFile).subscribe(
             (response) =>  {
               console.log('File upload successfully: ', response);
             },
             (error) => {
               console.error('File upload failed: ', error);
             }
         )
        }
    }*/
  }
}
