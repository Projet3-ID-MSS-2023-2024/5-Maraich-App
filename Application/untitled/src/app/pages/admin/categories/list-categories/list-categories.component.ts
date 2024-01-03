import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {TableModule} from "primeng/table";
import {Category} from "../../../../models/category";
import {CategoryService} from "../../../../services/category.service";
import {ButtonModule} from "primeng/button";
import {DialogService, DynamicDialogRef} from "primeng/dynamicdialog";
import {AddCategoriesComponent} from "../add-categories/add-categories.component";
import {UpdateCategoriesComponent} from "../update-categories/update-categories.component";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {InputTextModule} from "primeng/inputtext";
import {RippleModule} from "primeng/ripple";
import {ConfirmationService, MessageService} from "primeng/api";
import {forkJoin} from "rxjs";
import {ConfirmDialogModule} from "primeng/confirmdialog";

@Component({
  selector: 'app-list-categories',
  standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, ToastModule, ToolbarModule, InputTextModule, RippleModule, ConfirmDialogModule],
  templateUrl: './list-categories.component.html',
  styleUrl: './list-categories.component.css',
    providers: [DialogService, ConfirmationService, MessageService]
})
export class ListCategoriesComponent implements OnInit{
  categories: Category[] = [];
  selectedCategories: Category[] | null = [];
  category!:Category;

  ref: DynamicDialogRef | undefined;

  constructor(private categoryService: CategoryService, private dialogService: DialogService, private confirmationService: ConfirmationService, private messageService:MessageService) {}

  ngOnInit(): void {
    this.refreshCategories();
  }

  refreshCategories(){
      this.categoryService.getCategories().subscribe({
          next: (data) => {
            this.categories = data;
          },
          error: (error) => {
            console.error('Error fetching:', error);
          }
      });
  }

  showAdd(){
    this.ref = this.dialogService.open(AddCategoriesComponent,{
        header: 'Ajoutez une catégorie',
        data: {ref : this.ref, refreshCategories: this.refreshCategories.bind(this)}
    });
  }

  showEdit(id: number){
      this.ref = this.dialogService.open(UpdateCategoriesComponent, {
          header: 'Modifiez une catégorie',
          data: {ref: this.ref, id : id,  refreshCategories: this.refreshCategories.bind(this)}
      });
  }

  showDelete(id : number) {
    this.confirmationService.confirm({
        message: 'Êtes-vous sûr de vouloir supprimer la catégory ?',
        header: 'Confirmer',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          this.categoryService.deleteCategory(id).subscribe({
              next:()=>{
                this.messageService.add({
                    severity:'success',
                    summary: 'Succès',
                    detail: 'Catégory supprimé avec succès',
                });
                this.refreshCategories();
              }
          }
          );
        }
    })
  }

  showDeleteSelected(){
    this.confirmationService.confirm({
        message: 'Êtes-vous sûr de vouloir supprimer les catégories sélectionné ?',
        header: 'Confirmer',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          if (this.selectedCategories && this.selectedCategories.length > 0){
            const categoryIds = this.selectedCategories.map((category) => category.idCategory);

            const deleteObservables = categoryIds.map((categoryId) => {
              return this.categoryService.deleteCategory(categoryId)
            });

            forkJoin(deleteObservables).subscribe({
            next:() => {
              this.categories = this.categories.filter((c) => !categoryIds.includes(c.idCategory));

              this.messageService.add({
                  severity:'success',
                  summary: 'Succès',
                  detail: 'Catégories supprimés avec succès',
              });
            }
            });

            this.selectedCategories = null;
          }
        }
    });
  }
}
