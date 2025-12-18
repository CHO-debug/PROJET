import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { TokenService } from '../../core/services/token.service';

@Component({
  selector: 'app-dashboard-rh',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-rh.component.html',
  styleUrl: './dashboard-rh.component.css'
})
export class DashboardRhComponent {
  role: string | null = null;

  constructor(private readonly tokenService: TokenService) {
    this.role = this.tokenService.getUserRole();
  }
}