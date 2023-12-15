import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {Category} from "../../../../models/category";
import {CategoryService} from "../../../../services/category.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-add-categories',
  standalone: true,
    imports: [CommonModule, ButtonModule, FormsModule, InputTextModule, ReactiveFormsModule],
  templateUrl: './add-categories.component.html',
  styleUrl: './add-categories.component.css'
})
export class AddCategoriesComponent {
  nom: string = '';
  newCategory : Category = { idCategory:0, nomCategory: '', products: [] };

  constructor(private categoryService : CategoryService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {
  }

  onSubmit(){
    this.newCategory.nomCategory=this.nom;
    this.categoryService.postCategory(this.newCategory).subscribe(
      (response) => {
        console.log('Succès :', response);

        this.ref?.close();

        this.updateCategoryList();
      },
      (error) => {
        console.error('Error :' ,error);
      }
    );
  }

  private updateCategoryList(){
    const refreshCategories = this.config?.data?.refreshCategories;
    if(refreshCategories){
      refreshCategories();
    }
  }
}
