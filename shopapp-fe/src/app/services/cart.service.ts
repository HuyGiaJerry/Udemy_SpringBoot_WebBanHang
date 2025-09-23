import { Injectable } from "@angular/core";
import { Product } from "../models/product";
import { ProductService } from "./product.service";
import { Observable, of } from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CartService {
    private cart: Map<number,number> = new Map();

    constructor(private productService: ProductService) { 
        // lấy dữ liệu giả hàng khi khởi tạo service 
        const storedCart = localStorage.getItem('cart');
        if(storedCart) {
            this.cart = new Map(JSON.parse(storedCart));
        }
    }

    addCart(productId: number, quantity: number): void {
        debugger
        if(this.cart.has(productId)) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            this.cart.set(productId, this.cart.get(productId)! + quantity);
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
            this.cart.set(productId, quantity);
        }
        this.saveCartToLocalStorage();

    }


    getCart(): Map<number, number> {
        return this.cart;
    }

    private saveCartToLocalStorage(): void {
        debugger
        localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
    }

    clearCart(): void {
        this.cart.clear();
        this.saveCartToLocalStorage();
    }

}