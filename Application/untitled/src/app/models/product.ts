import {Category} from "./category";
import {Shop} from "./shop";
import {SafeUrl} from "@angular/platform-browser";

export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  picturePath: string;
  quantity: number;
  weight: number;
  isUnity: boolean;
  category?: Category;
  shop?: Shop;
  imageUrl?:SafeUrl;
  quantityUnity?: number;
  quantityWeight?: number;
}
