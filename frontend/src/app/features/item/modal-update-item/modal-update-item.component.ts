import { Component, Input, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { faSave } from '@fortawesome/free-regular-svg-icons';
import { ItemResponse } from '../../../models/response/item-response.model';
import { ItemService } from '../../../services/item.service';
import { ItemRequest } from '../../../models/request/item-request.model';
import { convertToDateTime } from '../../../core/utility';

@Component({
  selector: 'app-modal-update-item',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './modal-update-item.component.html'
})
export class ModalUpdateItemComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalUpdate!: () => void
  @Input() refresh!: () => void
  faSave = faSave

  apiErrors: string[] = []
  form: FormGroup
  item: ItemResponse | any

  constructor(
    private itemService: ItemService,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      name: ['', Validators.required],
      stock: ['', [Validators.required, Validators.min(0)]],
      price: ['', [Validators.required, Validators.min(0)]],
      isAvailable: [false, Validators.required],
      lastReStock: [null]
    })
  }

  ngOnInit(): void { this.get() }
  
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

      this.itemService.update(this.id, itemRequest).subscribe(
        (response) => { this.refresh() },
        (error) => { 
          const message = error.error?.message || 'Unknown error'
          this.apiErrors = Array.isArray(message) ? message : [message];
        }
      )
    }
  }

  private get() {
    this.itemService.get(this.id).subscribe(
      (response: ItemResponse) => { 
        this.item = response
        console.log(response);
        
        this.form.patchValue({
          name: response.name,
          price: response.price,
          stock: response.stock,
          lastReStock: convertToDateTime(response.lastReStock).split(" ")[0] || null,
          isAvailable: response.isAvailable
        })
      },
      (error) => { 
        const message = error.error?.message || 'Unknown error'
        this.apiErrors = Array.isArray(message) ? message : [message];
      }
    )
  }
}
