import {User} from "./user";
import {Shop} from "./shop";
import {Product} from "./product";
import {OrderProduct} from "./orderProduct";

export interface Order {
  id: number;
  orderDate: Date;
  totalPrice: number;
  redeemDate: Date;
  orderIsReady: boolean;
  readyDate?: Date;
  isArchived: boolean;
  customer: User;
  shopSeller: Shop;
  orderProducts: OrderProduct[];
}
