import { OrderDetail } from '../../models/order-detail';

export interface OrderResponse {
    id: number;
    user_id: number;
    fullname: string;
    email: string;
    phone_number: string;
    address: string;
    note: string;
    order_date: Date | null;
    status: string;
    total: number | string;
    shipping_method: string;
    shipping_adderess: string;
    payment_method: string;
    shipping_date: Date | null;
    order_details: OrderDetail[];
    message?: string;
}