import { ProductImage } from "./product.image";
export interface Product{
    id: number;
    name: string;
    description: string;
    price: number;
    thumpnail: string;
    url: string;
    category_id: number;
    product_images: ProductImage[];
}