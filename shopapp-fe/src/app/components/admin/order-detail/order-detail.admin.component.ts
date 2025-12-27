import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderResponse } from 'src/app/responses/order/order.response';
import { OrderService } from 'src/app/services/order.service';
import { environment } from 'src/app/environments/environment';
import { OrderDetail } from 'src/app/models/order-detail';
import { OrderDTO } from 'src/app/dtos/order/order.dto';

@Component({
    selector: 'app-order-detail-admin',
    templateUrl: './order-detail.admin.component.html',
    styleUrls: ['./order-detail.admin.component.scss']
})



export class OrderDetailAdminComponent implements OnInit {

    orderId: number = 0;
    orderResponse: OrderResponse = {
        id: 0,
        user_id: 0,
        fullname: '',
        email: '',
        phone_number: '',
        address: '',
        note: '',
        order_date: new Date(),
        status: '',
        total: 0,
        shipping_method: '',
        shipping_address: '',
        payment_method: '',
        shipping_date: new Date(),
        order_details: []

    };

    constructor(
        private orderService: OrderService,
        private route: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.getOrderDetails();
    }

    getOrderDetails(): void {
        debugger
        this.orderId = Number(this.route.snapshot.paramMap.get('id'));
        this.orderService.getOrderDetail(this.orderId).subscribe({
            next: (response: any) => {
                debugger
                this.orderResponse.id = response.id;
                this.orderResponse.user_id = response.user_id;
                this.orderResponse.fullname = response.fullname;
                this.orderResponse.email = response.email;
                this.orderResponse.phone_number = response.phone_number;
                this.orderResponse.address = response.address;
                this.orderResponse.note = response.note;
                this.orderResponse.order_date = response.order_date;
                this.orderResponse.status = response.status;
                this.orderResponse.total = response.total;
                this.orderResponse.shipping_method = response.shipping_method;
                this.orderResponse.shipping_address = response.shipping_address;
                this.orderResponse.payment_method = response.payment_method;
                this.orderResponse.shipping_date = response.shipping_date;
                debugger
                this.orderResponse.order_details = response.order_details.map((order_detail: OrderDetail) => {
                    order_detail.product.thumpnail = `${environment.apiUrl}/products/images/${order_detail.product.thumpnail}`;
                    return order_detail;
                });
                console.log('order_details:', response.order_details);
                console.log('Order details fetched successfully:', this.orderResponse);
            },
            complete: () => {
                debugger
            },
            error: (err: any) => {
                console.error('Error fetching order details:', err);
            }
        })


    }


    saveOrder(): void {
        debugger
        console.log('Updating order with status:', this.orderResponse.status);
        this.orderService
            .updateOrder(this.orderId, new OrderDTO(this.orderResponse))
            .subscribe({
                next: (response: any) => {
                    debugger
                    // Handle the successful update
                    console.log('Order updated successfully:', response);
                    // Navigate back to the previous page
                    this.router.navigate(['../'], { relativeTo: this.route });
                },
                complete: () => {
                    debugger;
                },
                error: (error: any) => {
                    // Handle the error
                    debugger
                    console.error('Error updating order:', error);
                }
            });
    }






}