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
      {
        path: 'interventions',
        pathMatch: 'full',
        loadComponent: () =>
          import('./features/dashboard/pages/interventions-list/interventions-list.component').then(
            (m) => m.InterventionsListComponent,
          ),
      },
      {
        path: 'interventions/:id',
        loadComponent: () =>
          import('./features/dashboard/pages/intervention/intervention.component').then(
            (m) => m.InterventionComponent,
          ),
      },
      {
        path: 'clients',
        pathMatch: 'full',
        loadComponent: () =>
          import('./features/dashboard/pages/clients/clients-list.component').then(
            (m) => m.ClientsListComponent,
          ),
      },
      {
        path: 'clients/:id',
        loadComponent: () =>
          import('./features/dashboard/pages/client-detail/client-detail.component').then(
            (m) => m.ClientDetailComponent,
          ),
      },
      {
        path: 'technicians',
        pathMatch: 'full',
        loadComponent: () =>
          import('./features/dashboard/pages/technicians/technicians-list.component').then(
            (m) => m.TechniciansListComponent,
          ),
      },
      {
        path: 'technicians/:id',
        loadComponent: () =>
          import('./features/dashboard/pages/technician-detail/technician-detail.component').then(
            (m) => m.TechnicianDetailComponent,
          ),
      },
      {
        path: 'analytics',
        loadComponent: () =>
          import('./features/dashboard/pages/analytics/analytics.component').then(
            (m) => m.AnalyticsComponent,
          ),
      },
      {
        path: 'exports',
        loadComponent: () =>
          import('./features/dashboard/pages/exports/exports.component').then(
            (m) => m.ExportsComponent,
          ),
      },
      {
        path: 'settings',
        loadComponent: () =>
          import('./features/dashboard/pages/settings/settings.component').then(
            (m) => m.SettingsComponent,
          ),
      },
    ],
  },
  { path: '**', redirectTo: '/login' },
];
