import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {ActivationComponent} from "./components/auth/activation/activation.component";
import {AccueilComponent} from "./temp/accueil/accueil.component";
import {AddCategoriesComponent} from "./components/shop/categories/add-categories/add-categories.component";
import {ListCategoriesComponent} from "./components/shop/categories/list-categories/list-categories.component";
import {UserManagementComponent} from "./components/admin/user-management/user-management.component";
import {AddProductComponent} from "./components/shop/products/add-product/add-product.component";
import {HomePageComponent} from "./pages/home-page/home-page.component";

export const routes: Routes = [
  { path: 'connexion', component: LoginComponent },
  { path: 'inscription', component: SignupComponent },
  { path: 'activation/:code', component: ActivationComponent },
  { path: 'activation', component: ActivationComponent },
  { path: 'accueil', component: AccueilComponent },
  { path: 'categories', component: ListCategoriesComponent},
  { path: 'admin/user', component: UserManagementComponent},
  { path: 'form-product', component: AddProductComponent},
  { path: 'home', component: HomePageComponent },
  { path: '**', redirectTo: '/connexion', pathMatch: 'full' },

];
