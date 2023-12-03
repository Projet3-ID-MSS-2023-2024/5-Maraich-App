import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {User} from "../../../models/user";
import {UserService} from "../../../services/user.service";
import {ConfirmationService, MessageService} from "primeng/api";
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

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, ToastModule, ToolbarModule, ButtonModule, FileUploadModule, TableModule, InputTextModule, RippleModule, TagModule, AdressFormatPipe, RankFormatPipe, DialogModule, FormsModule, ConfirmDialogModule],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
  providers: [MessageService , ConfirmationService],
  animations: []
})
export class UserManagementComponent {
  userDialog : boolean = false; //savoir si le dialog est ouvert ou non

  users! : User[]; //tableau de users
  user! : User | any;
  selectedUsers! : User[] | null; //va contenir les users que l'on veut supprimer

  submitted: boolean = false; //savoir si on soumis le formulaire (gestion erreur dans html)

  constructor(
      private userService: UserService,
      private messageService: MessageService,
      private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.userService.getAllUsers().subscribe(
        (data: User[]) => {
          this.users = data;
        },
        (error: any) => {
          console.error('Error fetching users', error);
        }
    );

  }

  /**
   * lorsque que l'on voudra ajouter un nouvel utilisateur
   */
  openNew() {
    this.user = {}; //pour etre sur que user est null
    this.submitted = false; //on le remet a false
    this.userDialog = true; //pour afficher le dialog
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
  }


  /**
   * Supprimer un user
   * @param user
   */
  deleteUser(user: User) {
    this.confirmationService.confirm({
      message: 'Êtes-vous sûr de vouloir supprimer ' + user.surname + '?',
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
  }

  /**
   * Modifier ou ajouter un user
   */
  saveUser() {
    this.submitted = true;

    if (this.user.name?.trim()) {
      if (this.user.idUser) {
        this.userService.updateUserAdmin(this.user).subscribe(updatedUser => {
          this.users[this.findIndexById(updatedUser.idUser)] = updatedUser;
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur mis à jour', life: 3000 });
          this.users = [...this.users];
          this.userDialog = false;
          this.user = {};
        });
      } else {
        this.userService.addUser(this.user).subscribe(newUser => {
          //newUser.image = 'user-placeholder.svg';
          this.users.push(newUser);
          this.messageService.add({ severity: 'success', summary: 'Succès', detail: 'Utilisateur créé', life: 3000 });
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
  getStatusSeverity(isActif: boolean): string | any {
    return isActif ? 'danger' : 'success';
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




}
