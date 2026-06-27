import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService, UserRole } from '../auth.service';
import { defaultRouteForRole } from '../auth.guard';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registerForm: FormGroup;
  logoUrl = 'https://avatars.githubusercontent.com/u/124091983';
  roles: UserRole[] = ['ADMINISTRATEUR', 'OPERATEUR', 'FOURNISSEUR'];
  isSubmitting = false;
  authError = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.formBuilder.group(
      {
        username: ['', [Validators.required, Validators.minLength(2)]],
        prenom: ['', [Validators.required, Validators.minLength(2)]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]],
        role: ['FOURNISSEUR', [Validators.required]]
      },
      { validators: this.passwordsMatch }
    );
  }

  onSubmit(): void {
    this.authError = '';

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const { confirmPassword, ...payload } = this.registerForm.value;
    this.isSubmitting = true;
    this.authService.signup(payload)
      .pipe(finalize(() => this.isSubmitting = false))
      .subscribe({
        next: (response) => {
          this.router.navigate([defaultRouteForRole(response.role)]);
        },
        error: () => {
          this.authError = 'Unable to create this account. Please check the username and role.';
        }
      });
  }

  private passwordsMatch(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;

    if (!password || !confirmPassword || password === confirmPassword) {
      return null;
    }

    return { passwordsMismatch: true };
  }
}
