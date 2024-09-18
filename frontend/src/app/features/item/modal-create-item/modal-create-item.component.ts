import { Component, Input } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { ItemService } from '../../../services/item.service';
import { ItemRequest } from '../../../models/request/item-request.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-modal-create-item',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './modal-create-item.component.html'
})
export class ModalCreateItemComponent {
  @Input() closeModalCreate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  
  constructor(
    private itemService: ItemService,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      stock: ['', Validators.required],
      isAvailable: [false, Validators.required],
      lastReStock: [null]
    })
  }

  onSubmit() {
    if (this.form.valid) {
      const itemRequest: ItemRequest = {
        name: this.form.get('name')?.value,
        price: this.form.get('price')?.value,
        stock: this.form.get('stock')?.value,
        isAvailable: this.form.get('isAvailable')?.value
      }

      const lastReStock = this.form.get('lastReStock')?.value
      if (lastReStock) itemRequest.lastReStock = lastReStock

      this.itemService.create(itemRequest).subscribe(
        (response) => { this.refresh() },
        (error) => { console.error(error) }
      )
    }
  }
}
