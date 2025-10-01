import { Product } from "./product";
export interface OrderDetail {
    id: number;
    product: Product;
    order_id: number;
    quantity: number;
    price: number;
    total   : number;
    color   : string;
}