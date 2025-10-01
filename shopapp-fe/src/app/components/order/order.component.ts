import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { OrderDTO } from '../../dtos/order/order.dto';
import { environment } from 'src/app/environments/environment';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/services/cart.service';
import { ProductService } from 'src/app/services/product.service';
import { OrderService } from 'src/app/services/order.service';
import { Form, FormBuilder, Validators,FormGroup } from '@angular/forms';
@Component({
  selector: 'app-order',

  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orderForm: FormGroup;
  cartItems: { product: Product; quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0
  orderData: OrderDTO = {
    user_id: 3,
    fullname:'',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_price: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private fb: FormBuilder
  ) {
    // Tạo FormGroup và các FormControl tương ứng 
    this.orderForm = this.fb.group({
      fullname: ['Nguyen Gia Huy', Validators.required],
      email: ['huy2004@gmail.com', [Validators.email]],
      phone_number: ['11223344556', [Validators.required, Validators.minLength(6)]],
      address: ['Ngo x Nha y', [Validators.required, Validators.minLength(5)]],
      note: ['chu y de vo'],
      shipping_method: ['express'],
      payment_method: ['cod']
    });

  }

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
          if (product) {
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
      complete: () => {
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

  placeOrder() {
    debugger
    if (this.orderForm.valid) {
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value,
      };
      this.orderData.cart_items = this.cartItems.map(item => ({
        product_id: item.product.id,
        quantity: item.quantity
      }));


      // Dữ liệu hợp lệ gửi yêu cầu đặt hàng
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response: any) => {
          console.log('Order placed successfully:', response);
          // Xử lý phản hồi từ server nếu cần
        },
        complete: () => {
          debugger
          this.calculateTotalAmount();
        },
        error: (error: any) => {
          console.error('Error placing order:', error);
          // Xử lý lỗi nếu cần
        }
      });

    } else {
      alert('Please fill in all required fields correctly.');
    }

  }

  applyCoupon(): void {
    // Logic áp dụng mã giảm giá
  }






}
