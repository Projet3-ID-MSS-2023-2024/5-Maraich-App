import { Address } from "./address";

export interface Users {
  idUser: number;
  firstName: string;
  surname: string;
  phoneNumber: string;
  address: Address;
  email: string;
}
