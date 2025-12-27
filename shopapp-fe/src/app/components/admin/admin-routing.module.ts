import { RouterModule, Routes } from "@angular/router";
import { AdminComponent } from "./admin.component";
import { OrderAdminComponent } from "./order/order.admin.component";
import { ProductAdminComponent } from "./product/product.admin.component";
import { CategoryAdminComponent } from "./category/category.admin.component";
import { NgModule } from "@angular/core";
import { OrderDetailAdminComponent } from "./order-detail/order-detail.admin.component";


const routes: Routes = [
    {
        path: '',
        component: AdminComponent,
        children: [
            {
                path: '',
                component: OrderAdminComponent
            },
            {
                path: 'orders',
                component: OrderAdminComponent
            },
            {
                path: 'orders/:id',
                component: OrderDetailAdminComponent
            },
            {
                path: 'products',
                component: ProductAdminComponent
            },
            {
                path: 'categories',
                component: CategoryAdminComponent
            }
        ]
    }
];


@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})

export class AdminRoutingModule { }