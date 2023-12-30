
import {Component, OnInit} from '@angular/core';
import {RankEnum} from "../../models/rankEnum";
import {MenuItem} from "primeng/api";
import {MenubarModule} from "primeng/menubar";
import {ChipsModule} from "primeng/chips";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../../services/auth.service";
import {NavigationEnd, Router} from "@angular/router";
import {authGuard} from "../../guard/auth.guard";
import {MaraicherManagementComponent} from "../../pages/admin/maraicher-management/maraicher-management.component";
import {UserManagementComponent} from "../../pages/admin/user-management/user-management.component";
import {ListCategoriesComponent} from "../../pages/admin/categories/list-categories/list-categories.component";

@Component({
  selector: 'app-navbar',
  standalone: true,

  imports: [
    MenubarModule,
    ChipsModule,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  constructor(private cookieService : CookieService, private authService : AuthService, private route : Router) {}
  userRank: RankEnum | undefined;
  items: MenuItem[] | undefined;
  isLogged: boolean = false;
  protected readonly RankEnum = RankEnum;

  ngOnInit() {
    //Run the initial code
    this.handleRouteChange();

    //Listen to route changes
    this.route.events.subscribe(event => {
      if(event instanceof NavigationEnd) {
        //Execute the code at each change of route
        this.handleRouteChange();
      }
    })

  }

  redirectToPanelMaraicher(){
    this.route.navigate(["maraicher/test"]);
  }

  redirectToPanelAdmin(){
    this.route.navigate(["admin/utilisateurs"]);
  }

  redirectToShoppingCart() {
    this.route.navigate(["panier"]);
  }

  userProfileClick() {
    if(this.isLogged){
      this.route.navigate(["modifier-profil"]);

    } else {
      this.route.navigate(["connexion"]);
    }
  }

  logout() {
    this.authService.logout().subscribe({
        next: (response: any) => {
          this.cookieService.deleteAll()
          this.isLogged = false;
          this.userRank = undefined;
          this.authService.userRank = this.userRank;
          this.route.navigate(["/accueil"]);
        },
        error: (error) => {
          this.cookieService.deleteAll()
          this.isLogged = false;
          this.userRank = undefined;
          this.authService.userRank = this.userRank;
          this.route.navigate(["/accueil"]);
        }
      }
    );
    this.handleRouteChange();
  }
  handleRouteChange(){
    this.userRank = this.authService.userRank;
    this.isLogged = !!this.userRank;
    if(this.route.url.startsWith("/maraicher")){
      this.items = this.itemsMaraicher();
    }
    else if(this.route.url.startsWith("/admin")){
      this.items = this.itemsAdmin();
    }
    else if(this.isLogged){
      this.items = this.itemsCustomer();
    }
    else {
      this.items = this.itemsUser();
    }

  }

  itemsCustomer() {
    return [
      {
        label: 'Accueil',
        icon: 'pi pi-fw pi-home',
        routerLink: "/accueil"
      },
      {
        label: 'Navbar client connecter',
        icon: 'pi pi-fw pi-home',
        routerLink: "/"

      }
    ];
  }

  itemsMaraicher(){
    return [
        {
          label: 'Accueil',
          icon: 'pi pi-fw pi-home',
          routerLink: "/accueil"
        },
        {
          label: 'Navbar maraicher',
          icon: 'pi pi-fw pi-home',
          routerLink: "/"

        }
      ];
  }

  itemsAdmin() {
    return [
      {
        label: 'Accueil',
        icon: 'pi pi-fw pi-home',
        routerLink: "/accueil"
      },
      {
        label: 'Gérer les catégories',
        icon: 'pi pi-fw pi-tag',
        routerLink: "/admin/categories"
      },
      {
        label: 'Gérer les utilisateurs',
        icon: 'pi pi-fw pi-users',
        routerLink: "/admin/utilisateurs"
      },
      {
        label: 'Gérer les maraichers',
        icon: 'pi pi-fw pi-shopping-cart',
        routerLink: "/admin/maraichers"
      },

    ];
  }

  itemsUser() {
    return [
      {
        label: 'Accueil',
        icon: 'pi pi-fw pi-home',
        routerLink: "/accueil"
      },
      {
        label: 'Navbar client non connecter',
        icon: 'pi pi-fw pi-home',
        routerLink: "/"

      }
    ];
  }
}
