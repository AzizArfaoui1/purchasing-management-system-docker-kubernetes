import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { Register } from './register/register';
import { authGuard } from './auth.guard';
import { DashboardShellComponent } from './dashboard-shell/dashboard-shell.component';
import { RolePageComponent } from './role-page/role-page.component';

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
        path: 'admin',
        component: RolePageComponent,
        canActivate: [authGuard],
        data: {
          roles: ['ADMINISTRATEUR'],
          title: 'Admin workspace',
          description: 'Administration pages are available for users with the ADMINISTRATEUR role.'
        }
      },
      {
        path: 'operator',
        component: RolePageComponent,
        canActivate: [authGuard],
        data: {
          roles: ['OPERATEUR'],
          title: 'Operator workspace',
          description: 'Operator pages are available for users with the OPERATEUR role.'
        }
      },
      {
        path: 'supplier',
        component: RolePageComponent,
        canActivate: [authGuard],
        data: {
          roles: ['FOURNISSEUR'],
          title: 'Supplier workspace',
          description: 'Supplier pages are available for users with the FOURNISSEUR role.'
        }
      }
    ]
  }
];
