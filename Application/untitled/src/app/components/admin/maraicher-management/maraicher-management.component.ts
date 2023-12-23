import {Component, OnInit} from '@angular/core';
import {AdressFormatPipe} from "../../../pipe/adress-format.pipe";
import {ButtonModule} from "primeng/button";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {DialogModule} from "primeng/dialog";
import {FileUploadModule} from "primeng/fileupload";
import {FormsModule} from "@angular/forms";
import {InputMaskModule} from "primeng/inputmask";
import {InputTextModule} from "primeng/inputtext";
import {NgIf} from "@angular/common";
import {PasswordModule} from "primeng/password";
import {RippleModule} from "primeng/ripple";
import {ConfirmationService, MessageService, SharedModule} from "primeng/api";
import {StepsModule} from "primeng/steps";
import {TableModule} from "primeng/table";
import {TagModule} from "primeng/tag";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {Shop} from "../../../models/shop";
import {ShopService} from "../../../services/shop.service";
import {forkJoin} from "rxjs";
import {User} from "../../../models/user";


@Component({
  selector: 'app-maraicher-management',
  standalone: true,
  imports: [
    AdressFormatPipe,
    ButtonModule,
    ConfirmDialogModule,
    DialogModule,
    FileUploadModule,
    FormsModule,
    InputMaskModule,
    InputTextModule,
    NgIf,
    PasswordModule,
    RippleModule,
    SharedModule,
    StepsModule,
    TableModule,
    TagModule,
    ToastModule,
    ToolbarModule
  ],
  providers: [MessageService , ConfirmationService],
  templateUrl: './maraicher-management.component.html',
  styleUrl: './maraicher-management.component.css'
})
export class MaraicherManagementComponent implements OnInit{

  shopDialog : boolean = false;
  shops!: Shop[];
  shop!: Shop | any;
  selectedShops!: Shop[] | null;
  submitted: boolean = false;
  statuses!: any[];

  constructor(
    private shopService : ShopService,
    private messageService : MessageService,
    private confirmationService: ConfirmationService
  ) {
  }

  ngOnInit() {
    this.getAllShops(); //recuperer les users

  }
  getAllShops() {
    this.shopService.getAllShops().subscribe(
      (data: Shop[]) => {
        this.shops = data;
      },
      (error: any) => {
        console.error('Error fetching shops', error);
      }
    );
  }

  openNew(){

  }

  editShop(shop: Shop) {
    this.shop = { ...shop }; // ... créé une copie
    this.shopDialog = true;
  }

  /**
   * Supprime un shop
   * @param shop
   */
  deleteShop(shop : Shop){
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer cette boutique: ' + shop.name + '?',
      header: 'Confirmer',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        this.shopService.deleteShop(shop.idShop).subscribe(() => {
          this.shops = this.shops.filter((val) => val.idShop !== shop.idShop) //le retire de la liste après suppression
          this.shop = {}; // on vide notre variable shop
          // Affichage du message de succès
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Boutique supprimée', life: 3000 });
        },
          (error) => {
            // En cas d'erreur
            this.messageService.add({ severity: 'danger', summary: 'Erreur', detail: 'Une erreur est survenue lors de la suppression de la boutique', life: 3000 });
          }
        );
      }
    })
  }

  /**
   * Supprimer toutes les boutiques sélectionnées
   */
  deleteSelectedShops(){
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les boutiques sélectionnées ?',
      header: 'Confirmer',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        if(this.selectedShops && this.selectedShops.length > 0){ // on vérifie qu'il y a bien des shops sélectionnés
          // Récupère les identifiants des shops sélectionnés
          const shopIds = this.selectedShops.map((shop) => shop.idShop);

          // Tableau d'Observables pour suivre les appels de suppression
          const deleteObservables = shopIds.map((shopId) => {
            // Supprimer le shop
            return this.shopService.deleteShop(shopId);
          });

          // forkJoin pour attendre que tous les appels soient terminés
          forkJoin(deleteObservables).subscribe(
            () => {
              // supprime les id de la liste
              this.shops = this.shops.filter((s)=> !shopIds.includes(s.idShop))
              // Message de succès une fois que toutes les suppressions sont terminées
              this.messageService.add({
                severity: 'success',
                summary: 'Succès',
                detail: 'Boutiques supprimées avec succès',
                life: 3000,
              });
            },
            (error) => {
              // En cas d'erreur
              this.messageService.add({
                severity: 'danger',
                summary: 'Erreur',
                detail: 'Une erreur est survenue lors de la suppression des utilisateurs',
                life: 3000,
              });
            }
          )
        }
      }
    })
  }

  getSeverity(status: boolean){
    switch (status){
      case true :
        return 'success'
      case false :
        return 'danger'
    }
  }


}
