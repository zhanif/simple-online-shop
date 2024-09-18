import { Component, Input, OnInit } from '@angular/core';
import { CustomerService } from '../../../services/customer.service';
import { CustomerResponse } from '../../../models/response/customer-response.model';
import { convertToDate } from '../../../core/utility';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal-view-customer',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './modal-view-customer.component.html'
})
export class ModalViewCustomerComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalView!: () => void
  customer: CustomerResponse | any

  constructor (
    private customerService: CustomerService
  ) {}

  ngOnInit() { this.get() }
  convertToDate(timestamp: number) { return convertToDate(timestamp) }

  get() {
    if (!this.id) return

    this.customerService.get(this.id).subscribe(
      (response: CustomerResponse) => {
        this.customer = response
      },
      (error) => {
        console.error(error)
      }
    )
  }
}
