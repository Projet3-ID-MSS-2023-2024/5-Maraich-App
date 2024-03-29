import {Routes} from '@angular/router';
import {LoginComponent} from "./pages/auth/login/login.component";
import {SignupComponent} from "./pages/auth/signup/signup.component";
import {ActivationComponent} from "./pages/auth/activation/activation.component";
import {UserManagementComponent} from "./pages/admin/user-management/user-management.component";
import {HomePageComponent} from "./pages/home-page/home-page.component";
import {authGuard} from "./guard/auth.guard";
import {MaraicherManagementComponent} from "./pages/admin/maraicher-management/maraicher-management.component";
import {ListProductsComponent} from "./pages/products/list-products/list-products.component";
import {PanierComponent} from "./pages/panier/panier.component";
import {ListOrderComponent} from "./pages/orders/vendor-side/list-order/list-order.component";
import {ViewOrderComponent} from "./pages/orders/vendor-side/view-order/view-order.component";
import {ClientOrderListComponent} from "./pages/orders/client-side/client-order-list/client-order-list.component";
import {ClientOrderViewComponent} from "./pages/orders/client-side/client-order-view/client-order-view.component";
import {ListCategoriesComponent} from "./pages/admin/categories/list-categories/list-categories.component";
import {EditUserProfileComponent} from "./pages/edit-user-profile/edit-user-profile.component";
import {EditShopProfileComponent} from "./pages/edit-shop-profile/edit-shop-profile.component";
import {OrderComponent} from "./pages/order/order.component";
import {AddRequestComponent} from "./pages/requests/add-request/add-request.component";
import {ListRequestsComponent} from "./pages/requests/list-requests/list-requests.component";

export const routes: Routes = [
  {
    path: 'maraicher', canActivate: [authGuard], children: [
      {path: 'modifier-profil', canActivate: [authGuard], component:EditShopProfileComponent},
      {path: 'commande/liste', canActivate: [authGuard], component: ListOrderComponent},
      {path: 'commande/consulter/:id', canActivate: [authGuard], component: ViewOrderComponent}
    ]
  },
  {
    path: 'admin', canActivate: [authGuard], children: [
      {path: 'utilisateurs', component: UserManagementComponent},
      {path: 'maraichers', component: MaraicherManagementComponent},
      {path: 'categories', component: ListCategoriesComponent},
      {path: 'requetes', component: ListRequestsComponent},
      {path: '**', redirectTo: 'utilisateurs', pathMatch: 'full'}
    ]
  },

  {path: 'accueil', canActivate: [authGuard], component: HomePageComponent},
  {path: 'ajouterRequete', canActivate: [authGuard], component: AddRequestComponent},
  {path: 'panier', canActivate: [authGuard],component: PanierComponent},
  {path: 'order', canActivate: [authGuard],component: OrderComponent},
  {path: 'shop/:shopId', canActivate: [authGuard], component:ListProductsComponent},
  {path: 'modifier-profil', canActivate: [authGuard], component:EditUserProfileComponent},

  {path: 'commande/client/liste', canActivate: [authGuard], component:ClientOrderListComponent},
  {path: 'commande/client/consulter/:id', canActivate: [authGuard], component:ClientOrderViewComponent},

  //Don't need the guard
  {path: 'connexion', component: LoginComponent},
  {path: 'inscription', component: SignupComponent},
  {path: 'activation/:code', component: ActivationComponent},
  {path: 'activation', component: ActivationComponent},
  {path: '**', redirectTo: '/accueil', pathMatch: 'full'}
];
