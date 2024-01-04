import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import {LoginComponent} from "./pages/auth/login/login.component";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {BnNgIdleService} from "bn-ng-idle";
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, LoginComponent, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  title = 'untitled';

  constructor(private bnIdle: BnNgIdleService, private authService : AuthService) {  }

  ngOnInit(): void {
    this.bnIdle.startWatching(15 * 60).subscribe((isTimedOut: boolean) => {
      if (isTimedOut) {
        this.authService.logout();
      }
    });
  }
}
