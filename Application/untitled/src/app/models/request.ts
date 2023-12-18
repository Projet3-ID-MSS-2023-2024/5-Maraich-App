import {User} from "./user";

export interface Request {
  idRequest: number;
  user: User;
  requestBody: string;
}
