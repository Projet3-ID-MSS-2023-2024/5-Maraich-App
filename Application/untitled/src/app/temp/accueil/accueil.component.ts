import {Component} from '@angular/core';
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
export class AccueilComponent {
  constructor(private route: Router, private authService: AuthService) {
  }


  onClick() {
    this.authService.logout().subscribe({
        next: (response: any) => {
          console.log(response)
        },
        error: (error) => {
          console.log(error);
        }
      }
    );
  }
}

