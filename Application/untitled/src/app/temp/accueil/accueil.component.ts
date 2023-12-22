import {Component, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {ButtonModule} from "primeng/button";

@Component({
  selector: 'app-accueil',
  standalone: true,
  imports: [CommonModule, ButtonModule],
  templateUrl: './accueil.component.html',
  styleUrl: './accueil.component.css'
})
export class AccueilComponent implements OnInit{

  constructor(private route: Router, private authService: AuthService) {
  }

  ngOnInit(): void {
    console.log(this.authService.userRank);
  }

  goMaraicher() {
    this.route.navigate(["/maraicher/produits"])
  }


  goAdmin() {
    this.route.navigate(["/admin/test"])
  }


}

