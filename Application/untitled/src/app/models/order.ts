import {User} from "./user";
import {Shop} from "./shop";
import {Product} from "./product";

export interface Order {
  idOrder: number;
  orderDate: Date;
  total: number;
  redeemDate: Date;
  orderIsReady: boolean;
  readyDate: Date;
  isArchived: boolean;
  customer: User;
  shopSeller: Shop;
  orderProucts: Product[];

}
