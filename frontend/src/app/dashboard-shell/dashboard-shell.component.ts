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
    { label: 'Dashboard', path: '/dashboard', roles: ['ADMINISTRATEUR', 'OPERATEUR', 'FOURNISSEUR'] },
    { label: 'Produits', path: '/produits', roles: ['ADMINISTRATEUR', 'OPERATEUR'] },
    { label: 'Stocks', path: '/stocks', roles: ['ADMINISTRATEUR', 'OPERATEUR'] },
    { label: 'Categories', path: '/categories', roles: ['ADMINISTRATEUR', 'OPERATEUR'] },
    { label: 'Fournisseurs', path: '/fournisseurs', roles: ['ADMINISTRATEUR', 'FOURNISSEUR'] },
    { label: 'Secteurs', path: '/secteurs', roles: ['ADMINISTRATEUR'] },
    { label: 'Operateurs', path: '/operateurs', roles: ['ADMINISTRATEUR'] },
    { label: 'Factures', path: '/factures', roles: ['ADMINISTRATEUR', 'OPERATEUR', 'FOURNISSEUR'] },
    { label: 'Reglements', path: '/reglements', roles: ['ADMINISTRATEUR', 'OPERATEUR'] }
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
