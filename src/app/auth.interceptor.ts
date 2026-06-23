import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';

const retriedRequests = new WeakSet<object>();

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const accessToken = authService.getAccessToken();
  const isAuthEndpoint = request.url.startsWith(authService.apiUrl);
  const isExternalRequest = /^https?:\/\//i.test(request.url);
  const isApiRequest = request.url.includes('/SpringMVC/') || !isExternalRequest;

  const authenticatedRequest = accessToken && isApiRequest && !isAuthEndpoint
    ? request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`
        }
      })
    : request;

  return next(authenticatedRequest).pipe(
    catchError((error: unknown) => {
      const isUnauthorized = error instanceof HttpErrorResponse && error.status === 401;

      if (!isUnauthorized || !isApiRequest || isAuthEndpoint || retriedRequests.has(request)) {
        return throwError(() => error);
      }

      retriedRequests.add(request);

      return authService.refreshAccessToken().pipe(
        switchMap(() => {
          const refreshedToken = authService.getAccessToken();

          if (!refreshedToken) {
            authService.logout();
            router.navigate(['/login']);
            return throwError(() => error);
          }

          return next(request.clone({
            setHeaders: {
              Authorization: `Bearer ${refreshedToken}`
            }
          }));
        }),
        catchError((refreshError: unknown) => {
          authService.logout();
          router.navigate(['/login']);
          return throwError(() => refreshError);
        })
      );
    })
  );
};
