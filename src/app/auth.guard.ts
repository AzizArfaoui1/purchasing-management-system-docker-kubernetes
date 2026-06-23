import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService, UserRole } from './auth.service';

export const authGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  const roles = route.data['roles'] as UserRole[] | undefined;

  if (roles?.length && !authService.hasRole(roles)) {
    return router.createUrlTree([defaultRouteForRole(authService.getRole())]);
  }

  return true;
};

export function defaultRouteForRole(role: UserRole | null): string {
  switch (role) {
    case 'ADMINISTRATEUR':
      return '/admin';
    case 'OPERATEUR':
      return '/operator';
    case 'FOURNISSEUR':
      return '/supplier';
    default:
      return '/login';
  }
}
