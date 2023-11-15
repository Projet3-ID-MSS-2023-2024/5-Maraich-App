import { Address } from "./address";
import { Users } from "./user";

export interface Shop {
  idBoutique: number;
  name: string;
  address: Address;
  picture: string;
  description: string;
  email: string;
  boss: Users;
}
