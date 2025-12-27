import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { OrderResponse } from "src/app/responses/order/order.response";
import { OrderService } from "src/app/services/order.service";


@Component({
    selector: 'app-order-admin',
    templateUrl: './order.admin.component.html',
    styleUrls: ['./order.admin.component.scss']
})

export class OrderAdminComponent implements OnInit {
    orders: OrderResponse[] = [];
    totalPages: number = 0;
    itemsPerPage: number = 10;
    currentPage: number = 0;
    pages: number[] = [];
    keyword: string = '';
    visiblePages: number[] = [];

    constructor(
        private orderService: OrderService,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
    }

    getAllOrders(keyword: string, page: number, limit: number): void {
        debugger
        this.orderService.getAllOrders(keyword, page, limit).subscribe({
            next: (response: any) => {
                debugger
                this.orders = response.orders;
                this.totalPages = response.totalPages;
                this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
            },
            complete: () => {
                debugger
            },
            error: (error: any) => {
                debugger
                alert('Error fetching orders: ' + error.message);
            }

        });
    }

    generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
        const maxVisiblePage = 5;
        const halfVisible = Math.floor(maxVisiblePage / 2);

        let startPage = Math.max(currentPage - halfVisible, 0);
        let endPage = Math.min(startPage + maxVisiblePage - 1, totalPages - 1);

        if (endPage - startPage + 1 < maxVisiblePage) {
            startPage = Math.max(endPage - maxVisiblePage + 1, 0);
        }

        return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
    }

    onPageChange(page: number): void {
        debugger
        this.currentPage = page;
        this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
    }

    deleteOrder(id: number) {
        const confirmation = window
            .confirm('Are you sure you want to delete this order?');
        if (confirmation) {
            debugger
            this.orderService.deleteOrder(id).subscribe({
                next: (response: any) => {
                    debugger
                    location.reload();
                },
                complete: () => {
                    debugger;
                },
                error: (error: any) => {
                    debugger;
                    console.error('Error fetching products:', error);
                }
            });
        }
    }
    viewDetails(order: OrderResponse) {
        debugger
        this.router.navigate(['/admin/orders', order.id]);
    }




}