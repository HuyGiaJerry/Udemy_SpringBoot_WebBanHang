import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { environment } from 'src/app/environments/environment';
import { OrderService } from 'src/app/services/order.service';
import { OrderDetail } from 'src/app/models/order-detail';
import { OrderResponse } from 'src/app/responses/order/order.response';

@Component({
  selector: 'app-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent implements OnInit {

  orderResponse: OrderResponse = {
    id: 1,
    user_id: 3,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    order_date: new Date(),
    status: '',
    total: '',
    shipping_method: '',
    shipping_adderess: '',
    payment_method: '',
    shipping_date: new Date(),
    order_details: []

  };

  constructor(
    private orderService: OrderService,
  ) { }

  ngOnInit(): void {
    this.getOrderDetails();
  }

  getOrderDetails(): void {
    debugger
    const orderId = 11;
    this.orderService.getOrderDetail(orderId).subscribe({
      next: (response: any) => {
        debugger
        this.orderResponse.id = response.id;
        this.orderResponse.user_id = response.user_id;
        this.orderResponse.fullname = response.fullname;
        this.orderResponse.email = response.email;
        this.orderResponse.phone_number = response.phone_number;
        this.orderResponse.address = response.address;
        this.orderResponse.note = response.note;
        if (Array.isArray(response.order_date) && response.order_date.length >= 3) {
          this.orderResponse.order_date = new Date(
            response.order_date[0],
            response.order_date[1] - 1,
            response.order_date[2]
          );
        } else if (response.order_date) {
          this.orderResponse.order_date = new Date(response.order_date);
        } else {
          this.orderResponse.order_date = null; // chỉ gán được nếu kiểu khai báo cho phép null
        }

        this.orderResponse.status = response.status;
        this.orderResponse.total = response.total;
        this.orderResponse.shipping_method = response.shipping_method;
        this.orderResponse.shipping_adderess = response.shipping_adderess;
        this.orderResponse.payment_method = response.payment_method;
        if (Array.isArray(response.shipping_date) && response.shipping_date.length >= 3) {
          this.orderResponse.shipping_date = new Date(
            response.shipping_date[0],
            response.shipping_date[1] - 1,
            response.shipping_date[2]
          );
        } else if (response.shipping_date) {
          this.orderResponse.shipping_date = new Date(response.shipping_date);
        } else {
          this.orderResponse.shipping_date = null; // chỉ gán được nếu kiểu khai báo cho phép null
        }
        debugger
        this.orderResponse.order_details = response.order_details.map((order_detail: OrderDetail) => {
          order_detail.product.thumpnail = `${environment.apiUrl}/products/images/${order_detail.product.thumpnail}`;
          return order_detail;
        });
        console.log('order_details:', response.order_details);
        console.log('Order details fetched successfully:', this.orderResponse);
      },
      error: (error) => {
        console.error('Error fetching order details:', error);
      }
    });
  }

}


