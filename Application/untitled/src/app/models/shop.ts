import {Address} from "./address";
import {User} from "./user";
import {Order} from "./order";
import {Product} from "./product";
import {SafeUrl} from "@angular/platform-browser";

export interface Shop {
  idShop: number;
  name: string;
  email: string;
  address: Address;
  picture: string;
  pictureUrl?: SafeUrl;
  description: string;
  shopIsOkay: boolean;
  enable: boolean;
  owner: User;
  orders: Order[];
  products?: Product[];
}
