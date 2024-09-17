import { Routes } from '@angular/router';
import { CustomerComponent } from './features/customer/customer.component';

export const routes: Routes = [
  { path: '', redirectTo: '', pathMatch: 'full' },
  { path: 'customer', component: CustomerComponent },
  { path: 'item', component: CustomerComponent },
  { path: 'order', component: CustomerComponent }
];
