import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";

export const routes: Routes = [
  { path: 'connexion', component: LoginComponent },
  { path: 'inscription', component: SignupComponent }
];
