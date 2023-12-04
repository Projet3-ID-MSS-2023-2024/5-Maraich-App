import {Address} from "./address";

export interface User {
  idUser: number;
  firstName: string;
  surname: string;
  phoneNumber: string;
  password: string;
  address: Address;
  email: string;
  isActif: boolean;



}
