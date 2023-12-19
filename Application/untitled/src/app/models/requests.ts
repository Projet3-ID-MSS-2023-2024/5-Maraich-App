import {User} from "./user";

export interface Requests {
  idRequest: number;
  user?: User;
  requestBody: string;
}
