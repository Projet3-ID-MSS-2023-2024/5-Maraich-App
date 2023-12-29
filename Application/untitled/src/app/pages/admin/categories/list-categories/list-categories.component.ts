import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {TableModule} from "primeng/table";
import {Category} from "../../../../models/category";
import {CategoryService} from "../../../../services/category.service";
import {ButtonModule} from "primeng/button";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {AddCategoriesComponent} from "../add-categories/add-categories.component";
import {DeleteCategoriesComponent} from "../delete-categories/delete-categories.component";
import {UpdateCategoriesComponent} from "../update-categories/update-categories.component";

@Component({
  selector: 'app-list-categories',
  standalone: true,
  imports: [CommonModule, TableModule, ButtonModule],
  templateUrl: './list-categories.component.html',
  styleUrl: './list-categories.component.css',
    providers: [DialogService]
})
export class ListCategoriesComponent implements OnInit{
  categories: Category[] = [];

  ref: DynamicDialogRef | undefined;

  constructor(private categoryService: CategoryService, public dialogService: DialogService) {}

  ngOnInit(): void {
    this.refreshCategories();
  }

  refreshCategories(){
      this.categoryService.getCategories().subscribe(
          (data) => {
              this.categories = data;
          },
          (error) => {
              console.error('Error fetching:', error);
          }
      );
  }

  showAdd(){
    this.ref = this.dialogService.open(AddCategoriesComponent,{
        header: 'Ajoutez une catégorie',
        data: {ref : this.ref, refreshCategories: this.refreshCategories.bind(this)}
    });
  }

  showDelete(id : number) {
    this.ref = this.dialogService.open(DeleteCategoriesComponent, {
        header: 'Supprimez une catégorie',
        data: {ref: this.ref, id : id,  refreshCategories: this.refreshCategories.bind(this)}
      });
  }

  showEdit(id: number){
      this.ref = this.dialogService.open(UpdateCategoriesComponent, {
          header: 'Modifiez une catégorie',
          data: {ref: this.ref, id : id,  refreshCategories: this.refreshCategories.bind(this)}
      });
  }
}
