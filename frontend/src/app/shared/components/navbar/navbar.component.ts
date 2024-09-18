import { Component } from "@angular/core";
import { RouterLink } from "@angular/router";

@Component({
  standalone: true,
  selector: 'app-navbar',
  imports: [
    RouterLink,
  ],
  templateUrl: './navbar.component.html',
})
export class NavbarComponent {}