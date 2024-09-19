import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { OrderResponse } from '../../../models/response/order-response.model';
import { OrderService } from '../../../services/order.service';
import { UpdateOrderRequest } from '../../../models/request/update-order-request.model';
import { convertToDateTime } from '../../../core/utility';

@Component({
  selector: 'app-modal-update-order',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './modal-update-order.component.html',
})
export class ModalUpdateOrderComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalUpdate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  order: OrderResponse | any

  constructor(
    private orderService: OrderService,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      quantity: ['', [Validators.required, Validators.min(1)]],
      date: ['', Validators.required]
    })
  }

  ngOnInit(): void {
    this.get()
  }

  onSubmit() {
    if (this.form.valid) {
      const orderRequest: UpdateOrderRequest = {
        quantity: this.form.get('quantity')?.value,
        date: this.form.get('date')?.value
      }

      this.orderService.update(this.id, orderRequest).subscribe(
        (response) => { this.refresh() },
        (error) => { 
          const message = error.error?.message || 'Unknown error'
          this.apiErrors = Array.isArray(message) ? message : [message];
        }
      )
    }
  }

  private get() {
    this.orderService.get(this.id).subscribe(
      (response: OrderResponse) => {
        this.order = response
        this.form.patchValue({
          quantity: response.quantity,
          date: convertToDateTime(response.date).split(' ')[0]
        })
      },
      (error) => {
        const message = error.error?.message || 'Unknown error'
        this.apiErrors = Array.isArray(message) ? message : [message];
      }
    )
  }
}
