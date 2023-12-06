import {Address} from "./address";
import {User} from "./user";
import {Product} from "./product";
import {Order} from "./order";

export interface Shop {
  idShop: number;
  name: string;
  email: string;
  address: Address;
  picture: string;
  description: string;
  shopIsOkay: boolean;
  enabled: boolean;
  owner: User;
  orders: Order[];
  products: Product[];
}
