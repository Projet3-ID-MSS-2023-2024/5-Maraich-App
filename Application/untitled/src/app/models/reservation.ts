import {Product} from "./product";
import {User} from "./user";

export interface Reservation {
  idReservation? : number
  product: Product;
  users: User;
  idShop : number;
  expirateDate? : number;
  reserveQuantity: number;
}
