import { Component, Input, OnInit } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertErrorComponent } from '../../../shared/components/alert-error/alert-error.component';
import { CommonModule } from '@angular/common';
import { faTrashCan } from '@fortawesome/free-regular-svg-icons';
import { ItemResponse } from '../../../models/response/item-response.model';
import { ItemService } from '../../../services/item.service';

@Component({
  selector: 'app-modal-delete-item',
  standalone: true,
  imports: [
    FontAwesomeModule,
    AlertErrorComponent,
    CommonModule
  ],
  templateUrl: './modal-delete-item.component.html'
})
export class ModalDeleteItemComponent implements OnInit {
  @Input() id!: number
  @Input() closeModalDelete!: () => void
  @Input() refresh!: () => void
  faTrashCan = faTrashCan
  item: ItemResponse | any

  apiErrors: string[] = []

  constructor(
    private itemService: ItemService
  ) {}

  ngOnInit(): void { this.get() }

  onSubmit() {
    this.itemService.delete(this.id).subscribe(
      (response) => { this.refresh() },
      (error) => { console.error(error) }
    )
  }

  private get() {
    this.itemService.get(this.id).subscribe(
      (response) => { this.item = response },
      (error) => { console.error(error) }
    )
  }
}
