import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { AlertErrorComponent } from "../../../shared/components/alert-error/alert-error.component";
import { CustomerService } from '../../../services/customer.service';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-modal-create-customer',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule
],
  templateUrl: './modal-create-customer.component.html'
})
export class ModalCreateCustomerComponent {
  @Input() closeModalCreate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  pic: File | undefined

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

      this.customerService.create(formData).subscribe(
        (response) => { this.refresh() },
        (error) => { console.error(error) }
      )
    }

  }
}
