import {Address} from "./address";
import {User} from "./user";
import {Order} from "./order";
import {Product} from "./product";

export interface Shop {
  idShop: number;
  name: string;
  email: string;
  address: Address;
  picture: string;
  description: string;
  shopIsOkay: boolean;
  enable: boolean;
  owner: User;
  orders: Order[];
  products: Product[];
}
