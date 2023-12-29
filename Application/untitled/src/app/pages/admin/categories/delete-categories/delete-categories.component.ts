import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ButtonModule} from "primeng/button";
import {CategoryService} from "../../../../services/category.service";
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

@Component({
  selector: 'app-delete-categories',
  standalone: true,
    imports: [CommonModule, ButtonModule],
  templateUrl: './delete-categories.component.html',
  styleUrl: './delete-categories.component.css'
})
export class DeleteCategoriesComponent {
  constructor(private categoryService: CategoryService, private ref: DynamicDialogRef, private config: DynamicDialogConfig) {}

  deleteCategory(){
    this.categoryService.deleteCategory(this.config?.data.id).subscribe(
        (response) => {
          console.log('Deleted:' , response);

          this.ref?.close();

          this.updateCategoryList();
        },
        (error) => {
          console.error('Failed:', error);
        }
    );
  }

  private updateCategoryList(){
    const refreshCategories = this.config?.data?.refreshCategories;
    if(refreshCategories){
      refreshCategories();
    }
  }

  close(){
      this.ref.close();
  }
}
