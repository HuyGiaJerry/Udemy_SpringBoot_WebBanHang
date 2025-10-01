import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../environments/environment";
import { OrderDTO } from "../dtos/order/order.dto";

@Injectable({
    providedIn: "root"
})
export class OrderService {
    private apiOrder = `${environment.apiUrl}/orders`;

    constructor(private http: HttpClient) { }

    placeOrder(orderData: OrderDTO): Observable<any> {
        return this.http.post<any>(this.apiOrder, orderData);
    }

    // Nếu cần lấy danh sách đơn hàng cho user
    getOrdersByUser(userId: number): Observable<any[]> {
        return this.http.get<any[]>(`${this.apiOrder}/user/${userId}`);
    }

    // Nếu cần lấy chi tiết đơn hàng
    getOrderDetail(orderId: number): Observable<any> {
        return this.http.get<any>(`${this.apiOrder}/${orderId}`);
    }
}