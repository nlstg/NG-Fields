import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'register',
    loadComponent: () => import('./features/register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: 'callback',
    loadComponent: () => import('./features/callback/callback.component').then((m) => m.CallbackComponent),
  },
  {
    path: 'unauthorized',
    loadComponent: () =>
      import('./features/unauthorized/unauthorized.component').then((m) => m.UnauthorizedComponent),
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard-layout.component').then((m) => m.DashboardLayoutComponent),
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'overview', pathMatch: 'full' },
      {
        path: 'overview',
        loadComponent: () =>
          import('./features/dashboard/pages/overview/overview.component').then(
            (m) => m.OverviewComponent,
          ),
      },
      {
        path: 'calendar',
        loadComponent: () =>
          import('./features/dashboard/pages/calendar/calendar.component').then(
            (m) => m.CalendarComponent,
          ),
      },
      {
        path: 'tasks',
        loadComponent: () =>
          import('./features/dashboard/pages/tasks/tasks.component').then(
            (m) => m.TasksComponent,
          ),
      },
      {
        path: 'kanban',
        loadComponent: () =>
          import('./features/dashboard/pages/kanban/kanban.component').then(
            (m) => m.KanbanComponent,
          ),
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./features/dashboard/pages/users/users.component').then(
            (m) => m.UsersComponent,
          ),
      },
      {
        path: 'roles',
        loadComponent: () =>
          import('./features/dashboard/pages/roles/roles.component').then(
            (m) => m.RolesComponent,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '/login' },
];
