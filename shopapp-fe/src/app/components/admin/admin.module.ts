import { CommonModule } from "@angular/common";
import { AdminComponent } from "./admin.component";
import { CategoryAdminComponent } from "./category/category.admin.component";
import { OrderDetailAdminComponent } from "./order-detail/order-detail.admin.component";
import { OrderAdminComponent } from "./order/order.admin.component";
import { ProductAdminComponent } from "./product/product.admin.component";
import { NgModule } from "@angular/core";
import { AdminRoutingModule } from "./admin-routing.module";
import { FormsModule } from "@angular/forms";



@NgModule({
    declarations:[
        AdminComponent,
        OrderAdminComponent,
        OrderDetailAdminComponent,
        ProductAdminComponent,
        CategoryAdminComponent
    ],
    imports: [
        AdminRoutingModule,
        CommonModule,
        FormsModule
    ]
})

export class AdminModule { }