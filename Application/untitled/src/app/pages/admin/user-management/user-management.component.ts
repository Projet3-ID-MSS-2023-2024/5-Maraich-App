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
import {forkJoin} from "rxjs";
import {RadioButtonModule} from "primeng/radiobutton";
import {Rank} from "../../../models/rank";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ToastModule, ToolbarModule, ButtonModule, FileUploadModule, TableModule, InputTextModule, RippleModule, TagModule, AdressFormatPipe, RankFormatPipe, DialogModule, FormsModule, ConfirmDialogModule, InputMaskModule, StepsModule, PasswordModule, DividerModule, KeyFilterModule, RadioButtonModule],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
  providers: [MessageService , ConfirmationService],
  animations: []
})
export class UserManagementComponent implements OnInit{
  userDialog : boolean = false; //savoir si le dialog est ouvert ou non

  users! : User[]; //tableau de users
  ranks! : Rank[]; //tableau de ranks
  user! : User | any;
  selectedUsers! : User[] | null; //va contenir les users que l'on veut supprimer
  confirmPassword: string | null = null;

  submitted: boolean = false; //savoir si on soumis le formulaire (gestion erreur dans html)

  items: MenuItem[] | any; //pour les etapes d'ajout/edit user
  currentStepIndex: number | any;

  idUser!:number; // id du user récupérer dans le token


//----> Les différents regex
  nameRegex : RegExp = /^[a-zA-ZÀ-ÿ-]+$/;
  roadRegex = /^[a-zA-Z0-9\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;
  postCodeRegex = /^[a-zA-Z0-9\s\-]+$/;
  numberRegex = /^[a-zA-Z0-9\s\-.,'()&]+$/;
  cityRegex = /^[a-zA-Z\s\-.,'()&àâäéèêëîïôöùûüçÀÂÄÉÈÊËÎÏÔÖÙÛÜÇ]+$/;
  passwordRegex = /^(?=.*[A-Z])(?=.*\d).{8,}$/;



  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private cookieService:CookieService
  ) {}

  ngOnInit() {
    this.getAllUsers(); //récupérer les users
    this.getAllRanks(); // récupérer les ranks
    this.formStep(); //etape du formulaire
    const jwtToken = this.cookieService.get('access_token');
    this.extractIdUserData(jwtToken);
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
    this.confirmPassword =null;
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

          //  tableau d'Observables pour suivre les appels de suppression
          const deleteObservables = userIds.map((userId) => {
            // Appelle le service pour supprimer l'id
            return this.userService.deleteUserById(userId);
          });

          //  forkJoin pour attendre que tous les appels soient terminés
          forkJoin(deleteObservables).subscribe(
            () => {
              // Supprime les id de la liste
              this.users = this.users.filter((u) => !userIds.includes(u.idUser));

              // Message de succès une fois que toutes les suppressions sont terminées
              this.messageService.add({
                severity: 'success',
                summary: 'Succès',
                detail: 'Utilisateurs supprimés avec succès',
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
          );

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
    // Sélectionne automatiquement le rang correspondant dans la liste des rangs
    if (this.ranks) {
      this.user.rank = this.ranks.find((rank) => rank.idRank === user.rank.idRank);
    }

    // Active le dialogue d'édition du user
    this.userDialog = true;
    this.currentStepIndex = 0;
    this.showPasswordInputField = false;
    this.confirmPassword =null;
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
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Une erreur est survenue lors de la suppression de l\'utilisateur', life: 3000 });
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
    this.confirmPassword =null;
  }

  /**
   * Modifier ou ajouter un user
   */
  saveUser() {
    this.submitted = true;

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
          this.confirmPassword =null;
        });
      } else {
        this.userService.addUser(this.user).subscribe(newUser => {
          //newUser.image = 'user-placeholder.svg';
          this.users.push(newUser);
          this.messageService.add({severity: 'success', summary: 'Succès', detail: 'Utilisateur créé', life: 3000});
          this.users = [...this.users];
          this.userDialog = false;
          this.user = {};
          this.confirmPassword =null;
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

  getAllRanks() {
    this.userService.getAllRanks().subscribe(
        (data: Rank[]) => {
          this.ranks = data;
        },
        (error: any) => {
          console.error('Error fetching ranks', error);
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
   * vérifie le form de chaque étape
   * exemple : étape 1, vérifie firstname, numéro...
   */
  validatePersonalStep() {
    this.submitted = true;

    const isEditModeWithPassword = this.isEditMode() && this.showPasswordInputField;
    const isPasswordValid = this.isValidPassword(this.user.password?.trim());

    if (this.currentStepIndex === 0) {
      if (
          this.user.surname?.trim() &&
          this.user.firstName?.trim() &&
          this.user.phoneNumber?.trim() &&
          ((!this.isEditMode() && this.user.password) || (isEditModeWithPassword && isPasswordValid) || !this.showPasswordInputField)
      ) {
        if (this.isEditMode()) {
          // Mode édition : vérifier si l'e-mail est valide et si les mots de passe correspondent
          if (this.user.email && this.isValidEmail(this.user.email.trim()) && (!isEditModeWithPassword || this.confirmPassword === this.user.password)) {
            this.nextStep();
            this.submitted = false;
          }
        } else {
          // Mode ajout : vérifier si l'e-mail est valide, n'existe pas déjà, et le mot de passe n'est pas vide et les mots de passe correspondent
          if (
              this.isValidEmail(this.user.email?.trim()) &&
              !this.emailExists(this.user.email?.trim()) &&
              this.user.password &&
              this.confirmPassword === this.user.password &&
              this.passwordRegex.test(this.user.password.trim())
          ) {
            this.nextStep();
            this.submitted = false;
          }
        }
      }
    } else if (this.currentStepIndex === 1) {
      if (
          this.user.address.road?.trim() &&
          this.user.address.postCode?.trim() &&
          this.user.address.city?.trim() &&
          this.user.address.number?.trim()
      ) {
        this.nextStep();
        this.submitted = false;
      }
    }
  }





  isValidPassword(password: string): boolean {
    const isValid = /^(?=.*[A-Z])(?=.*\d).{8,}$/.test(password);
    this.isPasswordValid = isValid;
    return isValid;
  }

  onPasswordChange(): void {
    // Mettez à jour isPasswordValid en fonction de votre expression régulière
    this.isPasswordValid = this.passwordRegex.test(this.user.password?.trim() || '');
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
   * Vérifier si l'email est présent parmi les autres users (en excluant le user actuel)
   * @param id
   * @param email
   */
  emailExist(id: number, email: string): boolean {
    const isEmailExist = this.users.some(user => user.idUser !== id && user.email === email);

    return isEmailExist;
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
      this.confirmPassword = null;
    }
  }

    /**
     * Récupérer l'id du user connecté
     * @param token
     */
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





    protected readonly RankEnum = RankEnum;
}
