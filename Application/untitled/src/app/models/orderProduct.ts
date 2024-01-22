import {Product} from "./product";
import {Order} from "./order";

export interface OrderProduct {
  id?: number;
  orders?: Order;
  product: Product;
  quantity: number;
}
