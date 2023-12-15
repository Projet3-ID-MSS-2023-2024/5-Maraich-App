import {Category} from "./category";

export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  picturePath: string;
  quantity: number;
  weight: number;
  isUnity: boolean;
  category: Category;
}
