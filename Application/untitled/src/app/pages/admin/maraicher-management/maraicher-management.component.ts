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
import {ConfirmationService, MenuItem, MessageService, SharedModule} from "primeng/api";
import {StepsModule} from "primeng/steps";
import {TableModule} from "primeng/table";
import {TagModule} from "primeng/tag";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {Shop} from "../../../models/shop";
import {ShopService} from "../../../services/shop.service";
import {forkJoin} from "rxjs";
import {User} from "../../../models/user";
import {Address} from "../../../models/address";
import {RadioButtonModule} from "primeng/radiobutton";
import {InputTextareaModule} from "primeng/inputtextarea";
import {KeyFilterModule} from "primeng/keyfilter";
import {ImageService} from "../../../services/image.service";
import {DomSanitizer} from "@angular/platform-browser";


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
    ToolbarModule,
    RadioButtonModule,
    InputTextareaModule,
    KeyFilterModule,
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
  selectedFile?: File | null;

  // Regex
  roadRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
  postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
  numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
  cityRegex = /^[a-zA-Z\s\-.,'()&]+$/;
  emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;


  items: MenuItem[] | any; //pour les étapes edit du shop
  currentStepIndex: number | any;

  constructor(
    private shopService : ShopService,
    private messageService : MessageService,
    private confirmationService: ConfirmationService,
    private addressFormat: AdressFormatPipe,
    private imageService: ImageService,
    private sanitizer:DomSanitizer
  ) {
  }

  ngOnInit() {
    this.getAllShops(); //recuperer les users
    this.formStep(); //etape du formulaire
  }

  getAllShops() {
    this.shopService.getAllShopsAdmin().subscribe(
      (data: Shop[]) => {
        this.shops = data.map(shop => ({
          ...shop,
          ownerFullName: `${shop.owner.surname} ${shop.owner.firstName}`,
          formattedAddress: this.addressFormat.transform(shop.address)
        }));
        this.loadImages();
      },
      (error: any) => {
        // En cas d'erreur
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Une erreur est survenue lors de l\'affichage des boutiques', life: 3000 });
      }
    );
  }

  openNew(){

  }

  editShop(shop: Shop) {
    this.shop = { ...shop }; // ... créé une copie
    this.shopDialog = true;
    this.currentStepIndex = 0; // on revient à la première étape
    this.showCurrentImage = true;
    this.selectedFile = null;
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
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Une erreur est survenue lors de la suppression de la boutique', life: 3000 });
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
                severity: 'error',
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

  formStep(){
    this.items = [
      {
        label: 'Information de la boutique',
      },
      {
        label: 'Adresse',
      },
      {
        label: 'Confirmation',
      }
    ];

    // Set the initial step
    this.currentStepIndex = 0;
  }

  isLastStep(): boolean {
    return this.currentStepIndex === this.items.length - 1;
  }

  nextStep(){//passer à l'étape suivante
    if (!this.isLastStep()) {
      this.currentStepIndex++;
    }
  }
  previousStep() { //étape précédente
    this.currentStepIndex--;
  }

  /**
   * Cacher la boite de dialogue
   */
  hideDialog() {
    this.shopDialog = false;
    this.submitted = false;
    this.currentStepIndex = 0;
    this.shop = {};
    this.showCurrentImage = true;
    this.selectedFile = null;
  }

  /**
   * Vérifier si l'email est présent parmi les autres boutiques (en excluant le shop actuel)
   * @param id
   * @param email
   */
  emailExist(id: number, email: string): boolean {
    const isEmailExist = this.shops.some(shop => shop.idShop !== id && shop.email === email);

    return isEmailExist;
  }

    isEmailValid(): boolean {
        return this.emailPattern.test(this.shop.email);
    }


  /**
   * Fonction appelée lorsqu'un fichier est sélectionné à l'aide d'un événement de sélection de fichier.
   * @param event L'événement de sélection de fichier contenant les informations sur le fichier sélectionné.
   */
  onFileSelected(event: any): void {
    // Récupérer le premier fichier de la liste des fichiers sélectionnés, s'il y en a.
    const fileInput = event.files && event.files.length > 0 ? event.files[0] : null;

    // Vérifier si un fichier a été sélectionné.
    if (fileInput) {
      // Spécifier la taille maximale autorisée du fichier en octets (100000 octets ici).
      const maxSizeInBytes = 100000;

      // Vérifier si la taille du fichier est inférieure à la taille maximale autorisée.
      if (fileInput.size < maxSizeInBytes) {
        // Affecter le fichier sélectionné à la variable selectedFile.
        this.selectedFile = event.files[0];
      } else {
        // Afficher un message d'erreur si la taille du fichier dépasse la limite autorisée.
        this.messageService.add({
          severity: 'danger',
          summary: 'Erreur',
          detail: 'La taille du fichier dépasse la limite autorisée.',
          life: 3000,
        });
      }
    }
  }

  saveShop() {
    console.log(this.shop);

    if (this.selectedFile) {
      // Si un fichier est sélectionné, téléchargez l'image.
      this.shopService.postImage(this.selectedFile).subscribe(
          (response) => {
            this.shop.picture = response.fileName;
            this.selectedFile = null;
            this.submitShop(); // Appel à la fonction de soumission une fois l'image téléchargée.
          },
          (error) => {
            console.error('Error adding image: ', error);
          }
      );
    } else {
      // Si aucun fichier n'est sélectionné, soumettez la boutique sans image.
      this.submitShop();
    }
  }


  submitShop(){
    console.log(this.shop);
    this.submitted = true;
    this.shopService.updateShop(this.shop).subscribe(updatedShop => {
      this.shops[this.findIndexById(updatedShop.idShop)] = updatedShop;
      this.messageService.add({
        severity: 'success',
        summary: 'Succès',
        detail: 'Boutique mise à jour',
        life: 3000
      });
      this.shops = [...this.shops];
      this.shopDialog = false;
      this.shop = {};
      this.loadImages();
    },
        (error) => {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Une erreur est survenue lors de la mise à jour de la boutique.',
            life: 3000
          });
        });

  }
  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.shops.length; i++) {
      if (this.shops[i].idShop === id) {
        index = i;
        break;
      }
    }

    return index;
  }

  showCurrentImage: boolean = true;

// Fonction pour basculer entre l'affichage de l'image actuelle et l'élément p-fileUpload
  toggleImageUpload() {
    this.showCurrentImage = !this.showCurrentImage;
  }


  loadImages(): void {
    const loadImagePromises = this.shops.map((shop) => {
      const fileName = shop.picture;
      return new Promise<void>((resolve) => {
        this.imageService.getImage(fileName).subscribe({
          next: (data: Blob) => {
            shop.pictureUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(data));
            resolve();
          },
          error: () => {
            console.error(`Error loading image ${fileName} from the backend.`);
            resolve();
          },
        });
      });
    });

  }


}
