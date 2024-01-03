import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {CategoryService} from "../../../../services/category.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {Category} from "../../../../models/category";

@Component({
  selector: 'app-update-categories',
  standalone: true,
  imports: [CommonModule, ButtonModule, FormsModule, InputTextModule, ReactiveFormsModule],
  templateUrl: './update-categories.component.html',
  styleUrl: './update-categories.component.css'
})
export class UpdateCategoriesComponent implements OnInit{
  nom: string = '';
  modifiedCategory: Category = {idCategory:0, nomCategory: '', products: []};

  constructor(private categoryService: CategoryService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {}

  ngOnInit(): void {
    this.getCategoryById();
  }

  onSubmit(){
    this.modifiedCategory.nomCategory=this.nom;
    this.categoryService.updateCategory(this.modifiedCategory).subscribe(
        () => {
      this.ref?.close();

      this.updateCategoryList();
    },
        (error) => {
          console.error('Error :', error);
        }
    );
  }

  private updateCategoryList(){
    const refreshCategories = this.config?.data?.refreshCategories;
    if(refreshCategories){
      refreshCategories();
    }
  }

  private getCategoryById(){
    this.categoryService.getCategoryById(this.config.data.id).subscribe(
        (data) => {
          this.modifiedCategory = data;
          this.nom = this.modifiedCategory.nomCategory;

          console.log(this.modifiedCategory);
        },
        (error) => {
          console.error('Erreur :', error);
        }
    );
  }

}
