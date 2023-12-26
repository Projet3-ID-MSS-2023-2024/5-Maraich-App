import {Routes} from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {ActivationComponent} from "./components/auth/activation/activation.component";
import {AccueilComponent} from "./temp/accueil/accueil.component";
import {ListCategoriesComponent} from "./components/shop/categories/list-categories/list-categories.component";
import {UserManagementComponent} from "./components/admin/user-management/user-management.component";
import {AddProductComponent} from "./components/shop/products/add-product/add-product.component";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AddRequestComponent} from "./components/requests/add-request/add-request.component";
import {authGuard} from "./guard/auth.guard";
import {MaraicherComponent} from "./temp/maraicher/maraicher.component";
import {AdminComponent} from "./temp/admin/admin.component";
import {MaraicherManagementComponent} from "./components/admin/maraicher-management/maraicher-management.component";
import {ListProductsComponent} from "./components/shop/products/list-products/list-products.component";

export const routes: Routes = [

  {
    path: 'maraicher', canActivate: [authGuard], children: [
      {path: 'test', component: MaraicherComponent},
    ]
  },
  {
    path: 'admin', canActivate: [authGuard], children: [
      {path: 'test', component: AdminComponent},
    ]
  },

  {path: 'categories', canActivate: [authGuard], component: ListCategoriesComponent},
  {path: 'admin/user', canActivate: [authGuard], component: UserManagementComponent},
  {path: 'admin/maraicher', canActivate: [authGuard], component: MaraicherManagementComponent},
  {path: 'form-product', canActivate: [authGuard], component: AddProductComponent},
  {path: 'home', canActivate: [authGuard], component: HomePageComponent},
  {path: 'addRequest', canActivate: [authGuard], component: AddRequestComponent},

  {path: 'shop/:shopId', component:ListProductsComponent},

  //Don't need the guard
  {path: 'accueil',canActivate: [authGuard], component: AccueilComponent},
  {path: 'connexion', component: LoginComponent},
  {path: 'inscription', component: SignupComponent},
  {path: 'activation/:code', component: ActivationComponent},
  {path: 'activation', component: ActivationComponent},
  {path: '**', redirectTo: '/accueil', pathMatch: 'full'}

];
