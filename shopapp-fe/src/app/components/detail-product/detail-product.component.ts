import { Component } from '@angular/core';
import { Product } from 'src/app/models/product';
import { ProductService } from 'src/app/services/product.service';
import { OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { ProductImage } from 'src/app/models/product.image';
import { CartService } from 'src/app/services/cart.service';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
@Component({
  selector: 'app-detail-product',

  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss']
})
export class DetailProductComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1; // Số lượng mặc định khi thêm vào giỏ hàng
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    // Lấy productId từ URL
    //this.cartService.clearCart(); // Xóa giỏ hàng khi vào trang chi tiết sản phẩm
    debugger
    const idParam = this.activatedRoute.snapshot.paramMap.get('id');
    // const idParam = 5; // Giả sử bạn lấy được id từ URL
    if (idParam != null) {
      this.productId = +idParam;
    }

    if (!isNaN(this.productId)) {
      this.productService.getDetailProduct(this.productId).subscribe({
        next: (response: any) => {
          console.log("api response: ", response);
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiUrl}/products/images/${product_image.image_url}`;
            });
          }
          debugger
          this.product = response;
          console.log('product_images:', this.product?.product_images);
          this.showImage(0);

        },
        complete: () => {
          debugger
        },
        error: (error: any) => {
          console.error('Error fetching product details:', error);
        }
      })
    }
    else {
      console.error('Invalid product ID,idParam:', idParam);
    }


  }


  showImage(index: number): void {
    debugger
    if (this.product && this.product.product_images && this.product.product_images.length > 0) {
      // Đảm bảo index nằm trong khoảng hợp lệ 
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }
      // index = index <= 0 ? 0 : this.product.product_images.length - 1 ;
      this.currentImageIndex = index;
    }
  }

  thumbnailClick(index: number): void {
    debugger
    this.currentImageIndex = index;
  }

  nextImage(): void {
    debugger
    this.showImage(this.currentImageIndex + 1);
  }
  previousImage(): void {
    debugger
    this.showImage(this.currentImageIndex - 1);
  }

  addToCart(): void {
    debugger
    if (this.product) {
      this.cartService.addCart(this.productId, this.quantity);
    } else {
      console.error('Không thể thêm sản phẩm vào giỏ hàng vì product = null');
    }

  }

  increaseQuantity(): void {
    debugger
    this.quantity++;
  }

  decreaseQuantity(): void {
    debugger
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  buyNow(): void {
    debugger
    this.cartService.addCart(this.productId, this.quantity);
    this.router.navigate(['/orders']);
  }


}
