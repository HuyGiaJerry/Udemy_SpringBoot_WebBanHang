import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../environments/environment";
import { OrderDTO } from "../dtos/order/order.dto";
import { OrderResponse } from "../responses/order/order.response";

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

    getAllOrders(keyword: string, page: number, limit: number): Observable<OrderResponse[]> {
        const params = new HttpParams()
            .set('keyword', keyword)
            .set('page', page.toString())
            .set('limit', limit.toString());

        return this.http.get<any>(`${this.apiOrder}/get-orders-by-keyword`, { params });
    }

    updateOrder(orderId: number, orderData: OrderDTO): Observable<any> {
        return this.http.put<any>(`${this.apiOrder}/${orderId}`, orderData);
    }

    deleteOrder(orderId: number): Observable<any> {
        return this.http.delete<any>(`${this.apiOrder}/${orderId}`,{responseType: 'text' as 'json'});
    }



}