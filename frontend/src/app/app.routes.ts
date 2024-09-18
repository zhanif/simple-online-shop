import { Routes } from '@angular/router';
import { CustomerComponent } from './features/customer/customer.component';
import { ItemComponent } from './features/item/item.component';

export const routes: Routes = [
  { path: '', redirectTo: '', pathMatch: 'full' },
  { path: 'customer', component: CustomerComponent },
  { path: 'item', component: ItemComponent },
  { path: 'order', component: CustomerComponent }
];
