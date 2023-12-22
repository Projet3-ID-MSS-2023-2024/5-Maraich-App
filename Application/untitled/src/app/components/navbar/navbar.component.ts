import {Component, OnInit} from '@angular/core';
import {RankEnum} from "../../models/rankEnum";
import {MenuItem} from "primeng/api";
import {MenubarModule} from "primeng/menubar";
import {ChipsModule} from "primeng/chips";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {CookieService} from "ngx-cookie-service";
import {AuthService} from "../../services/auth.service";
import {NavigationEnd, Router} from "@angular/router";

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
    this.route.navigate(["maraicher/produits"]);
  }

  redirectToPanelAdmin(){
    this.route.navigate(["admin/test"]);
  }

  logout() {
    this.authService.logout().subscribe({
        next: (response: any) => {
          this.cookieService.deleteAll()
          this.isLogged = false;
          this.userRank = undefined;
          this.authService.userRank = this.userRank;
          this.route.navigate(["/accueil"]);
          this.handleRouteChange();
        },
        error: (error) => {
        }
      }
    );
    this.handleRouteChange();
  }

  clickProfiUser(){
    if(!this.isLogged){
      this.route.navigate(["/connexion"]);
    }
    // IMPLEMENTER LE ELSE QUI REDIREGERA VERS LE PROFIL SI CONNECTER
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
        routerLink: "/"

      },
      {
        label: 'Client',
        icon: ''
      }
    ];
  }

  itemsMaraicher(){
    return [
        {
          label: 'Accueil',
          icon: 'pi pi-fw pi-home',
          routerLink: "/"

        },
        {
          label: 'Maraicher',
          icon: ''
        }
      ];
  }

  itemsAdmin() {
    return [
      {
        label: 'Accueil',
        icon: 'pi pi-fw pi-home',
        routerLink: "/"

      },
      {
        label: 'Admin',
        icon: ''
      }
    ];
  }

  itemsUser() {
    return [
      {
        label: 'Accueil',
        icon: 'pi pi-fw pi-home',
        routerLink: "/"

      },
      {
        label: 'Non connecter',
        icon: ''
      }
    ];
  }


}
