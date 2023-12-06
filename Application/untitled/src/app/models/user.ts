import {Address} from "./address";
import {Order} from "./order";
import {Rank} from "./rank";

export interface User {
  idUser: number;
  firstName: string;
  surname: string;
  phoneNumber: string;
  password: string;
  address: Address;
  email: string;
  rank : Rank;
  isActif: boolean;
  orders : Order[];



}
