import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { AlertErrorComponent } from "../../../shared/components/alert-error/alert-error.component";
import { CustomerService } from '../../../services/customer.service';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { CustomerResponse } from '../../../models/response/customer-response.model';

@Component({
  selector: 'app-modal-update-customer',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './modal-update-customer.component.html'
})
export class ModalUpdateCustomerComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalUpdate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  pic: File | undefined
  customer: CustomerResponse | any

  constructor(
    private customerService: CustomerService,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      phone: ['', Validators.required],
      address: ['', Validators.required],
      isActive: [false, Validators.required],
    })
  }

  ngOnInit(): void { this.get() }

  onFileSelected(event: any) {
    const file: File = event.target.files[0]
    this.pic = file ? file : undefined
  }

  onSubmit() {
    if (this.form.valid) {
      const formData = new FormData()
      formData.append('name', this.form.get('name')?.value)
      formData.append('phone', this.form.get('phone')?.value)
      formData.append('address', this.form.get('address')?.value)
      formData.append('isActive', this.form.get('isActive')?.value)
      if (this.pic) formData.append('pic', this.pic as File)

      this.customerService.update(this.id, formData).subscribe(
        (response) => { this.refresh() },
        (error) => { 
          const message = error.error?.message || 'Unknown error'
          this.apiErrors = Array.isArray(message) ? message : [message];
        }
      )
    }
  }

  private get() {
    this.customerService.get(this.id).subscribe(
      (response: CustomerResponse) => {
        this.customer = response
        this.form.patchValue({
          name: response.name,
          phone: response.phone,
          address: response.address,
          isActive: response.isActive
        })
      },
      (error) => { 
        const message = error.error?.message || 'Unknown error'
        this.apiErrors = Array.isArray(message) ? message : [message];
      }
    )
  }
}
