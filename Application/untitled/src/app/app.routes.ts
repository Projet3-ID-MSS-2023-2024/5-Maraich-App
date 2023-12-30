import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/auth/login/login.component";
import {SignupComponent} from "./pages/auth/signup/signup.component";
import {ActivationComponent} from "./pages/auth/activation/activation.component";
import {UserManagementComponent} from "./pages/admin/user-management/user-management.component";
import {AddProductComponent} from "./pages/products/add-product/add-product.component";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {AddRequestComponent} from "./components/requests/add-request/add-request.component";
import {authGuard} from "./guard/auth.guard";
import {MaraicherManagementComponent} from "./pages/admin/maraicher-management/maraicher-management.component";
import {ListProductsComponent} from "./pages/products/list-products/list-products.component";
import {PanierComponent} from "./pages/panier/panier.component";
import {ListCategoriesComponent} from "./pages/admin/categories/list-categories/list-categories.component";
import {EditUserProfileComponent} from "./pages/edit-user-profile/edit-user-profile.component";

export const routes: Routes = [
  {
    path: 'maraicher', canActivate: [authGuard], children: [
    ]
  },
  {
    path: 'admin', canActivate: [authGuard], children: [
      {path: 'utilisateurs', component: UserManagementComponent},
      {path: 'maraichers', component: MaraicherManagementComponent},
      {path: 'categories', component: ListCategoriesComponent}
    ]
  },

  {path: 'accueil', canActivate: [authGuard], component: HomePageComponent},
  {path: 'addRequest', canActivate: [authGuard], component: AddRequestComponent},
  {path: 'panier', canActivate: [authGuard],component: PanierComponent},
  {path: 'shop/:shopId', canActivate: [authGuard], component:ListProductsComponent},
  {path: 'modifier-profil', canActivate: [authGuard], component:EditUserProfileComponent},

  //Don't need the guard
  {path: 'connexion', component: LoginComponent},
  {path: 'inscription', component: SignupComponent},
  {path: 'activation/:code', component: ActivationComponent},
  {path: 'activation', component: ActivationComponent},
  {path: '**', redirectTo: '/accueil', pathMatch: 'full'}
];
