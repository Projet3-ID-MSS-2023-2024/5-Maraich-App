import {Product} from "./product";

export interface Category {
  idCategory: number;
  nomCategory: string;
  products?: Product[];
}
