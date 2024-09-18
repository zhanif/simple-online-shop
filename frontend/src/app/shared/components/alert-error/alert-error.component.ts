import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-alert-error',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './alert-error.component.html',
})
export class AlertErrorComponent {
  @Input() title: string = 'Error'
  @Input() errors: string[] = []
}
