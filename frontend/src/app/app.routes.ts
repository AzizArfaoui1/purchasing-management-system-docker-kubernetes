import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { Register } from './register/register';
import { authGuard } from './auth.guard';
import { DashboardShellComponent } from './dashboard-shell/dashboard-shell.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ManagementPageComponent } from './management-page/management-page.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: Register
  },
  {
    path: '',
    component: DashboardShellComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'produits',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'OPERATEUR'], entity: 'produits' }
      },
      {
        path: 'stocks',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'OPERATEUR'], entity: 'stocks' }
      },
      {
        path: 'categories',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'OPERATEUR'], entity: 'categories' }
      },
      {
        path: 'fournisseurs',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'FOURNISSEUR'], entity: 'fournisseurs' }
      },
      {
        path: 'secteurs',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR'], entity: 'secteurs' }
      },
      {
        path: 'operateurs',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR'], entity: 'operateurs' }
      },
      {
        path: 'factures',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'OPERATEUR', 'FOURNISSEUR'], entity: 'factures' }
      },
      {
        path: 'reglements',
        component: ManagementPageComponent,
        canActivate: [authGuard],
        data: { roles: ['ADMINISTRATEUR', 'OPERATEUR'], entity: 'reglements' }
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'supplier',
        redirectTo: 'fournisseurs'
      },
      {
        path: 'operator',
        redirectTo: 'produits'
      },
      {
        path: 'admin',
        redirectTo: 'dashboard'
      },
      {
        path: '**',
        redirectTo: 'dashboard'
      }
    ]
  }
];
