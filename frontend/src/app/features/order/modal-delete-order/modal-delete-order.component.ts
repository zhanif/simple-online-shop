import { Component, Input, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { CommonModule } from '@angular/common';
import { faTrashCan } from '@fortawesome/free-regular-svg-icons';
import { OrderResponse } from '../../../models/response/order-response.model';
import { OrderService } from '../../../services/order.service';
import { convertToDateTime } from '../../../core/utility';

@Component({
  selector: 'app-modal-delete-order',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    CommonModule
  ],
  templateUrl: './modal-delete-order.component.html'
})
export class ModalDeleteOrderComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalDelete!: () => void
  @Input() refresh!: () => void
  faTrashCan = faTrashCan
  order: OrderResponse | any

  apiErrors: string[] = []

  constructor(
    private orderService: OrderService
  ) {}

  ngOnInit(): void { this.get() }
  convertToDate(timestamp: number): string { return convertToDateTime(timestamp).split(' ')[0] }

  onSubmit() {
    this.orderService.delete(this.id).subscribe(
      (response) => { this.refresh() },
      (error) => { 
        const message = error.error?.message || 'Unknown error'
        this.apiErrors = Array.isArray(message) ? message : [message];
      }
    )
  }

  private get() {
    this.orderService.get(this.id).subscribe(
      (response) => { this.order = response },
      (error) => { 
        const message = error.error?.message || 'Unknown error'
        this.apiErrors = Array.isArray(message) ? message : [message];
      }
    )
  }
}
