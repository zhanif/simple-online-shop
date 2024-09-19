import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { NavigationEnd, Router, RouterLink, RouterOutlet } from '@angular/router';
import { FooterComponent } from './shared/components/footer/footer.component';
import { filter } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    NavbarComponent,
    FooterComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'online-shop';
  isMainRoute = true

  constructor (
    private router: Router
  ) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe(() => {
      this.isMainRoute = (this.router.url == '/' || this.router.url == '')
    })
  }
}
