import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/auth/login/login.component";
import {SignupComponent} from "./pages/auth/signup/signup.component";
import {ActivationComponent} from "./pages/auth/activation/activation.component";
import {AccueilComponent} from "./temp/accueil/accueil.component";
import {ListCategoriesComponent} from "./components/shop/categories/list-categories/list-categories.component";
import {UserManagementComponent} from "./pages/admin/user-management/user-management.component";
import {AddProductComponent} from "./components/shop/products/add-product/add-product.component";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AddRequestComponent} from "./components/requests/add-request/add-request.component";
import {authGuard} from "./guard/auth.guard";
import {MaraicherComponent} from "./temp/maraicher/maraicher.component";
import {AdminComponent} from "./temp/admin/admin.component";
import {MaraicherManagementComponent} from "./pages/admin/maraicher-management/maraicher-management.component";
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
      {path: 'user', component: UserManagementComponent},
      {path: 'maraicher', component: MaraicherManagementComponent},
    ]
  },

  {path: 'categories', canActivate: [authGuard], component: ListCategoriesComponent},
  {path: 'home', canActivate: [authGuard], component: HomePageComponent},
  {path: 'addRequest', canActivate: [authGuard], component: AddRequestComponent},
  {path: 'shop/:shopId',canActivate: [authGuard], component:ListProductsComponent},
  {path: 'accueil',canActivate: [authGuard], component: AccueilComponent},

  //Don't need the guard
  {path: 'connexion', component: LoginComponent},
  {path: 'inscription', component: SignupComponent},
  {path: 'activation/:code', component: ActivationComponent},
  {path: 'activation', component: ActivationComponent},
  {path: '**', redirectTo: '/accueil', pathMatch: 'full'}

];
