import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { AuthService, LoginResponse } from '../../../core/services/auth.service';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loading = false;
  error: string | null = null;
  success: string | null = null;
  response: LoginResponse | null = null;

  visualSrc = 'https://www.greatplacetowork.in/wp-content/uploads/2024/01/Banner-Investing-in-people-1024x768.png';

  form!: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly authService: AuthService,
    private readonly tokenService: TokenService,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  get emailCtrl() {
    return this.form.get('email');
  }

  get passwordCtrl() {
    return this.form.get('password');
  }

  onVisualError(): void {
    if (this.visualSrc !== '/login-meeting.jpg' && this.visualSrc !== '/login-meeting.svg') {
      this.visualSrc = '/login-meeting.jpg';
      return;
    }

    if (this.visualSrc === '/login-meeting.jpg') {
      this.visualSrc = '/login-meeting.svg';
    }
  }

  submit(): void {
    this.error = null;
    this.success = null;
    this.response = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;

    // ✅ CORRECTION ICI
    const payload = {
      email: this.form.value.email!,
      motDePasse: this.form.value.password!
    };

    this.authService.login(payload).subscribe({
      next: (res: LoginResponse) => {
        this.response = res;

        const token = res.token;
        const id = res.id;
        const role = res.role;

        if (token) {
          this.tokenService.setToken(token);

          if (id) {
            this.tokenService.setUserId(id);
          }

          if (role) {
            this.tokenService.setRole(role);
          }

          this.success = 'Connexion réussie.';

          const isRh = role ? role.toString().toLowerCase().includes('rh') : false;
          if (isRh) {
            this.router.navigateByUrl('/dashboard/rh');
          } else {
            this.router.navigateByUrl('/dashboard');
          }
        } else {
          this.success = 'Connexion réussie, mais aucun token trouvé.';
        }

        this.loading = false;
      },

      error: (e: unknown) => {
        if (e instanceof HttpErrorResponse) {
          this.error = typeof e.error === 'string' ? e.error : e.error?.message || `Erreur HTTP ${e.status}`;
        } else if (e instanceof Error) {
          this.error = e.message;
        } else {
          this.error = 'Erreur lors du login.';
        }
        this.loading = false;
      }
    });
  }
}
