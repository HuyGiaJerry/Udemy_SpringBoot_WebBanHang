import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeComponent } from './components/home/home.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OrderComponent } from './components/order/order.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AuthGuardFn } from './components/guards/auth.guard';

const routes: Routes = [
    { path: '', component: HomeComponent }, // <-- Sửa thành HomeComponent
    { path: 'home', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'products/:id', component: DetailProductComponent },
    { path: 'orders', component: OrderComponent , canActivate: [AuthGuardFn]},
    { path: 'orders/:id', component: OrderDetailComponent , canActivate: [AuthGuardFn]}
    
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }