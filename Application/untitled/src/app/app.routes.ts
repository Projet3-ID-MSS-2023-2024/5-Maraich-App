import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {ActivationComponent} from "./components/auth/activation/activation.component";
import {AccueilComponent} from "./temp/accueil/accueil.component";

export const routes: Routes = [
  { path: 'connexion', component: LoginComponent },
  { path: 'inscription', component: SignupComponent },
  { path: 'activation/:code', component: ActivationComponent },
  { path: 'activation', component: ActivationComponent },
  { path: 'accueil', component: AccueilComponent },
  { path: '**', redirectTo: '/connexion', pathMatch: 'full' },
];
