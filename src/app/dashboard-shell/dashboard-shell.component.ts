import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService, UserRole } from '../auth.service';

interface NavItem {
  label: string;
  path: string;
  roles: UserRole[];
}

@Component({
  selector: 'app-dashboard-shell',
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './dashboard-shell.component.html',
  styleUrl: './dashboard-shell.component.css'
})
export class DashboardShellComponent {
  readonly navItems: NavItem[] = [
    { label: 'Admin', path: '/admin', roles: ['ADMINISTRATEUR'] },
    { label: 'Operator', path: '/operator', roles: ['OPERATEUR'] },
    { label: 'Supplier', path: '/supplier', roles: ['FOURNISSEUR'] }
  ];
  isLoggingOut = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  get role(): UserRole | null {
    return this.authService.getRole();
  }

  get userId(): number | null {
    return this.authService.getUserId();
  }

  allowed(item: NavItem): boolean {
    return this.authService.hasRole(item.roles);
  }

  logout(): void {
    this.isLoggingOut = true;
    this.authService.logoutRequest()
      .pipe(finalize(() => {
        this.authService.logout();
        this.isLoggingOut = false;
        this.router.navigate(['/login']);
      }))
      .subscribe({ error: () => undefined });
  }
}
