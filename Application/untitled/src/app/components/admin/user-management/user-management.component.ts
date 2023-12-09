import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {User} from "../../../models/user";
import {UserService} from "../../../services/user.service";
import {ConfirmationService, MenuItem, MessageService} from "primeng/api";
import {ToastModule} from "primeng/toast";
import {ToolbarModule} from "primeng/toolbar";
import {ButtonModule} from "primeng/button";
import {FileUploadModule} from "primeng/fileupload";
import {TableModule} from "primeng/table";
import {InputTextModule} from "primeng/inputtext";
import {RippleModule} from "primeng/ripple";
import {TagModule} from "primeng/tag";
import {AdressFormatPipe} from "../../../pipe/adress-format.pipe";
import {RankEnum} from "../../../models/rankEnum";
import {RankFormatPipe} from "../../../pipe/rank-format.pipe";
import {DialogModule} from "primeng/dialog";
import {FormsModule} from "@angular/forms";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {InputMaskModule} from "primeng/inputmask";
import {StepsModule} from "primeng/steps";
import {PasswordModule} from "primeng/password";
import {DividerModule} from "primeng/divider";
import {KeyFilterModule} from "primeng/keyfilter";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ToastModule, ToolbarModule, ButtonModule, FileUploadModule, TableModule, InputTextModule, RippleModule, TagModule, AdressFormatPipe, RankFormatPipe, DialogModule, FormsModule, ConfirmDialogModule, InputMaskModule, StepsModule, PasswordModule, DividerModule, KeyFilterModule],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
  providers: [MessageService , ConfirmationService],
  animations: []
})
export class UserManagementComponent implements OnInit{
  userDialog : boolean = false; //savoir si le dialog est ouvert ou non

  users! : User[]; //tableau de users
  user! : User | any;
  selectedUsers! : User[] | null; //va contenir les users que l'on veut supprimer

  submitted: boolean = false; //savoir si on soumis le formulaire (gestion erreur dans html)

  items: MenuItem[] | any; //pour les etapes d'ajout/edit user
  currentStepIndex: number | any;


//regex
  nameRegex : RegExp = /^[a-zA-ZÀ-ÿ-]+$/;
  roadRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
  postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
  numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
  cityRegex = /^[a-zA-Z\s\-.,'()&]+$/;


  constructor(
      private userService: UserService,
      private messageService: MessageService,
      private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.getAllUsers(); //recuperer les users
    this.formStep(); //etape du formulaire

  }

  /**
   * lorsque que l'on voudra ajouter un nouvel utilisateur
   */
  openNew() {
    this.user = {
      address: {} // Initialisez user.address avec un objet vide
    }; //pour etre sur que user est null
    this.submitted = false; //on le remet a false
    this.userDialog = true; //pour afficher le dialog
      this.currentStepIndex = 0;
  }

  /**
   * Supprimer les users séléctionnés
   */
  deleteSelectedUsers() {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer les utilisateurs sélectionnés?',
      header: 'Confirmer',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        // Vérifie s'il y a des utilisateurs sélectionnés
        if (this.selectedUsers && this.selectedUsers.length > 0) {
          // Récupère les identifiants des utilisateurs sélectionnés
          const userIds = this.selectedUsers.map((user) => user.idUser);

          // Parcourir chaque id de user
          userIds.forEach((userId) => {
            //on appelle le service pour supprimer l'id
            this.userService.deleteUserById(userId).subscribe(
                () => {
                  //on supprime également l'id de la liste
                  this.users = this.users.filter((u) => u.idUser !== userId);

                  // Message de succès
                  this.messageService.add({
                    severity: 'success',
                    summary: 'Succès',
                    detail: 'Utilisateur supprimé',
                    life: 3000,
                  });
                },
                (error) => {
                  // en cas d'erreur
                  console.error('Erreur lors de la suppression de l\'utilisateur', error);
                }
            );
          });
          // Réinitialise la liste des utilisateurs sélectionnés
          this.selectedUsers = null;
        }
      }
    });
  }


  /**
   * Lorsque l'on voudra editer un user
   * @param user
   */
  editUser(user : User) {
    // clone le user mit en paramètre
    this.user = { ...user };
    // Active le dialogue d'édition du user
    this.userDialog = true;
    this.currentStepIndex = 0;
    this.showPasswordInputField = false;
  }


  /**
   * Supprimer un user
   * @param user
   */
  deleteUser(user: User) {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer ' + user.surname + ' ' + user.firstName + '?',
      header: 'Confirmer',
      icon: 'pi pi-exclamation-triangle',
      accept: () => {
        // on supprime le user de la BDD
        this.userService.deleteUserById(user.idUser).subscribe(
            () => {
              // on supprime le user de la liste
              this.users = this.users.filter((val) => val.idUser !== user.idUser);
              this.user = {};
              // Affichage du message de succès
              this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur supprimé', life: 3000 });
            },
            (error) => {
              // En cas d'erreur
              console.error('Erreur lors de la suppression de l\'utilisateur', error);
            }
        );
      }
    });
  }

  /**
   * Cacher la boite de dialogue
   */
  hideDialog() {
    this.userDialog = false;
    this.submitted = false;
    this.currentStepIndex = 0;
    this.user = {};
    this.showPasswordInputField = false;
  }

  /**
   * Modifier ou ajouter un user
   */
  saveUser() {
      this.submitted = true;

      console.log(this.user);

      if (this.user.surname?.trim()) {
        if (this.user.idUser) {
          this.userService.updateUserAdmin(this.user).subscribe(updatedUser => {
            this.users[this.findIndexById(updatedUser.idUser)] = updatedUser;
            this.messageService.add({
              severity: 'success',
              summary: 'Succès',
              detail: 'Utilisateur mis à jour',
              life: 3000
            });
            this.users = [...this.users];
            this.userDialog = false;
            this.user = {};
          });
        } else {
          this.userService.addUser(this.user).subscribe(newUser => {
            //newUser.image = 'user-placeholder.svg';
            this.users.push(newUser);
            this.messageService.add({severity: 'success', summary: 'Succès', detail: 'Utilisateur créé', life: 3000});
            this.users = [...this.users];
            this.userDialog = false;
            this.user = {};
          });
        }
      }

  }

  findIndexById(id: number): number {
    let index = -1;
    for (let i = 0; i < this.users.length; i++) {
      if (this.users[i].idUser === id) {
        index = i;
        break;
      }
    }

    return index;
  }


  /**
   * Couleur à mettre pour actif ou inactif
   * @param status
   */
  getStatusSeverity(actif: boolean): string | any {
    return actif ? 'success' : 'danger';
  }

  getRankSeverity(rank: RankEnum): string | any {
    switch (rank) {
      case RankEnum.CUSTOMER:
        return 'info'; // Choisissez la gravité appropriée
      case RankEnum.MARAICHER:
        return 'success'; // Choisissez la gravité appropriée
      case RankEnum.ADMINISTRATOR:
        return 'danger'; // Choisissez la gravité appropriée
      default:
        return '';
    }
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe(
      (data: User[]) => {
        this.users = data;
      },
      (error: any) => {
        console.error('Error fetching users', error);
      }
    );
  }


  formStep(){
    this.items = [
      {
        label: 'Informations personnelles',
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




  isPasswordValid = false; //a mettre dans le diasbled de suivant si bug de mdp

  /**
   * à mettre si bug avec le mdp lors de l'ajout
   */
  /*
  onPasswordChange() {
    // Update isPasswordValid based on the password validity
    this.isPasswordValid = this.isValidPassword(this.user.password?.trim());
  }
*/



    /**
     * vérifie le form de chaque étape
     * exemple : étape 1, vérifie firstname, numéro...
     */
    validatePersonalStep() {
      this.submitted = true;

      if (
        this.user.surname?.trim() &&
        this.user.firstName?.trim() &&
        this.user.phoneNumber?.trim() &&
        this.isValidPassword(this.user.password?.trim()) &&
        this.isPasswordValid // Check the password validity
      ) {
        if (this.isEditMode()) {
          // Mode édition : vérifier si l'e-mail est valide
          if (this.user.email && this.isValidEmail(this.user.email.trim())) {
            this.nextStep();
            this.submitted = false;
          }
        } else {
          // Mode ajout : vérifier si l'e-mail est valide et n'existe pas déjà
          if (this.isValidEmail(this.user.email?.trim()) && !this.emailExists(this.user.email?.trim())) {
            this.nextStep();
            this.submitted = false;
          }
        }
      }
    }

  isValidPassword(password: string): boolean {
    const isValid = /^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password);
    this.isPasswordValid = isValid;
    return isValid;
  }


  /**
     * pour la validation d'email, si il a le bon format
     * @param email
     */
    isValidEmail(email: string): boolean {
        // Expression régulière pour la validation de l'e-mail
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    /**
     * vérifie si l'email existe deja
     * @param email
     */
    emailExists(email: string): boolean {
      // Assuming this.users is the array you are checking
      return this.users && this.users.some(user => user.email === email);
    }


    /**
     * Retourne true si l'ID de l'utilisateur est défini, ce qui signifie que vous êtes en mode édition
     */
    isEditMode(): boolean {

        return this.user && this.user.idUser !== undefined;
    }


  showPasswordInputField = false;


  /**
   * si on est en edit, boutton pour modifier mdp
   */
  showPasswordInput(): void {
    if (this.isEditMode()) {
      // Si on est en mode édition, affiche directement le champ de mot de passe.
      this.showPasswordInputField = true;
    } else {
      // Si on est en mode ajout, bascule entre afficher et cacher le champ de mot de passe.
      this.showPasswordInputField = !this.showPasswordInputField;
    }
  }

  btnPwdSubmitted(): void {
    this.showPasswordInput();
    if (this.showPasswordInputField) {
      // Si le bouton "modifier mdp" est cliqué, réinitialisez le mot de passe à null.
      this.user.password = null;
    }
  }








}
