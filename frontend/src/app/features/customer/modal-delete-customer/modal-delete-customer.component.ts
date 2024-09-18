import { Component, Input, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faTrashCan } from '@fortawesome/free-regular-svg-icons';
import { CustomerService } from '../../../services/customer.service';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { CommonModule } from '@angular/common';
import { CustomerResponse } from '../../../models/response/customer-response.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-modal-delete-customer',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    CommonModule
  ],
  templateUrl: './modal-delete-customer.component.html'
})
export class ModalDeleteCustomerComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalDelete!: () => void
  @Input() refresh!: () => void
  faTrashCan = faTrashCan
  customer: CustomerResponse | any

  apiErrors: string[] = []
  pic: File | undefined

  constructor(
    private customerService: CustomerService
  ) {}

  ngOnInit(): void { this.get() }

  onSubmit() {
    this.customerService.delete(this.id).subscribe(
      (response) => { this.refresh() },
      (error) => { console.error(error) }
    )
  }

  private get() {
    this.customerService.get(this.id).subscribe(
      (response) => { this.customer = response },
      (error) => { console.error(error) }
    )
  }
}
