import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
@Component({
  selector: 'app-order-confirm',
  templateUrl: './order-confirm.component.html',
  styleUrls: ['./order-confirm.component.scss']
})
export class OrderConfirmComponent implements OnInit {
  cartItems: { product: Product; quantity: number }[] = [];
  totalAmount: number = 0;
  couponCode: string = '';


  constructor(
    private cartService: CartService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    // Lấy danh sách sản phẩm từ giỏ hàng 
    debugger
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());
    // Gọi service để lấy thông tin dựa theo danh sách ID
    debugger
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        debugger
        this.cartItems = productIds.map((productId) => {
          debugger
          const product = products.find(p => p.id === productId);
          if(product) {
            product.thumpnail = `${environment.apiUrl}/products/images/${product.thumpnail}`;
          }
          return {
            product: product!,
            quantity: cart.get(productId) || 0
          };
        })
        this.calculateTotalAmount();
        // console.log('haha');
      },
      complete:() => {
        debugger
      },
      error: (err: any) => {
        debugger
        console.error('Error fetching products by IDs:', err);
      }

  });  
  }
  calculateTotalAmount(): void {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => {
        return total + (item.product.price * item.quantity);
      },
      0
    );
  }

  applyCoupon(): void {
    // Logic áp dụng mã giảm giá
    console.log('Applying coupon code:', this.couponCode);
  }

}


