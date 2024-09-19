import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { OrderService } from '../../../services/order.service';
import { CreateOrderRequest } from '../../../models/request/create-order-request.model';
import { NgSelectModule } from "@ng-select/ng-select"
import { SearchOptionResponse } from '../../../models/response/search-option-response.model';
import { debounceTime, Subject, switchMap } from 'rxjs';
import { CustomerService } from '../../../services/customer.service';
import { ItemService } from '../../../services/item.service';

@Component({
  selector: 'app-modal-create-order',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule,
    NgSelectModule
  ],
  templateUrl: './modal-create-order.component.html',
  styleUrl: '../order.component.css',
  encapsulation: ViewEncapsulation.None
})
export class ModalCreateOrderComponent implements OnInit {
  @Input() closeModalCreate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  searchCustomers: Array<SearchOptionResponse> = []
  searchCustomerStr = new Subject<string>()
  searchItems: Array<SearchOptionResponse> = []
  searchItemStr = new Subject<string>()

  constructor(
    private customerService: CustomerService,
    private itemService: ItemService,
    private orderService: OrderService,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      customerId: ['', Validators.required],
      itemId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      date: [null, Validators.required]
    })

    this.searchCustomerStr.pipe(
      debounceTime(300),
      switchMap((term: string) => this.customerService.search(term))
    )
    .subscribe((data: Array<SearchOptionResponse>) => {
      this.searchCustomers = data
    })
    this.searchItemStr.pipe(
      debounceTime(300),
      switchMap((term: string) => this.itemService.search(term))
    )
    .subscribe((data: Array<SearchOptionResponse>) => {
      this.searchItems = data
    })
  }

  ngOnInit(): void {
    this.onSearchCustomer('')
    this.onSearchItem('')
  }

  onSubmit() {
    if (this.form.valid) {
      const orderRequest: CreateOrderRequest = {
        customerId: this.form.get('customerId')?.value,
        itemId: this.form.get('itemId')?.value,
        date: this.form.get('date')?.value,
        quantity: this.form.get('quantity')?.value
      }
      
      this.orderService.create(orderRequest).subscribe(
        (response) => { this.refresh() },
        (error) => {
          const message = error.error?.message || 'Unknown error'
          this.apiErrors = Array.isArray(message) ? message : [message];
        }
      )
    }
  }

  onSearchCustomer(term: string) { this.searchCustomerStr.next(term) }
  onSelectCustomerChange(selected: any) { this.form.patchValue({ customerId: selected.id }) }
  onSearchItem(term: string) { this.searchItemStr.next(term) }
  onSelectItemChange(selected: any) { this.form.patchValue({ itemId: selected.id }) }
}
