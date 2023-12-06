import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {UserManagementComponent} from "./components/admin/user-management/user-management.component";

export const routes: Routes = [
  { path: 'connexion', component: LoginComponent },
  { path: 'inscription', component: SignupComponent },
  { path: 'admin/user', component: UserManagementComponent},
  { path: '', redirectTo: 'connexion', pathMatch: 'full' },
  { path: '**', redirectTo: '/connexion', pathMatch: 'full' }
];
