import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IconComponent } from '../../../../shared/ui/icon/icon.component';

interface User {
  id: string;
  name: string;
  email: string;
  role: 'ADMIN' | 'MANAGER' | 'TECHNICIAN';
  status: 'active' | 'inactive';
  joinedDate: Date;
}

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule, IconComponent],
  template: `
    <div class="flex flex-col gap-6 p-4 md:p-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold tracking-tight">Utilisateurs</h1>
          <p class="text-sm text-muted-foreground">Gérer les membres de l'organisation et leurs accès</p>
        </div>
        <button class="inline-flex items-center gap-2 rounded-md bg-primary px-4 py-2 text-sm font-medium text-primary-foreground hover:bg-primary/90">
          <app-icon name="circle-plus" />
          Ajouter
        </button>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <div class="relative">
          <span class="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground">
            <app-icon name="search" />
          </span>
          <input type="text" placeholder="Rechercher un utilisateur..."
            class="pl-9 pr-4 py-2 rounded-md border border-input bg-background text-sm w-64"
            [ngModel]="searchQuery()" (ngModelChange)="searchQuery.set($event)" />
        </div>
        <select class="rounded-md border border-input bg-background px-3 py-2 text-sm"
          [ngModel]="roleFilter()" (ngModelChange)="roleFilter.set($event)">
          <option value="all">Tous les rôles</option>
          <option value="ADMIN">Admin</option>
          <option value="MANAGER">Manager</option>
          <option value="TECHNICIAN">Technicien</option>
        </select>
        <select class="rounded-md border border-input bg-background px-3 py-2 text-sm"
          [ngModel]="statusFilter()" (ngModelChange)="statusFilter.set($event)">
          <option value="all">Tous les statuts</option>
          <option value="active">Actif</option>
          <option value="inactive">Inactif</option>
        </select>
        <span class="ml-auto text-sm text-muted-foreground">{{ filteredUsers().length }} utilisateur(s)</span>
      </div>

      <div class="rounded-lg border bg-card shadow-sm">
        <div class="overflow-x-auto">
          <table class="w-full text-sm">
            <thead>
              <tr class="border-b bg-muted/50 text-left">
                <th class="px-6 py-3 text-xs font-medium text-muted-foreground uppercase">Utilisateur</th>
                <th class="px-6 py-3 text-xs font-medium text-muted-foreground uppercase">Rôle</th>
                <th class="px-6 py-3 text-xs font-medium text-muted-foreground uppercase">Statut</th>
                <th class="px-6 py-3 text-xs font-medium text-muted-foreground uppercase">Inscription</th>
                <th class="px-6 py-3 text-xs font-medium text-muted-foreground uppercase text-right">Actions</th>
              </tr>
            </thead>
            <tbody class="divide-y">
              @for (user of filteredUsers(); track user.id) {
                <tr class="hover:bg-muted/30 transition-colors">
                  <td class="px-6 py-4">
                    <div class="flex items-center gap-3">
                      <div class="flex h-10 w-10 items-center justify-center rounded-full bg-secondary font-medium text-sm">
                        {{ user.name.charAt(0) }}{{ user.name.split(' ')[1]?.charAt(0) || '' }}
                      </div>
                      <div>
                        <p class="text-sm font-medium">{{ user.name }}</p>
                        <p class="text-xs text-muted-foreground">{{ user.email }}</p>
                      </div>
                    </div>
                  </td>
                  <td class="px-6 py-4">
                    <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium"
                      [class.bg-purple-100]="user.role === 'ADMIN'" [class.text-purple-800]="user.role === 'ADMIN'"
                      [class.bg-blue-100]="user.role === 'MANAGER'" [class.text-blue-800]="user.role === 'MANAGER'"
                      [class.bg-green-100]="user.role === 'TECHNICIAN'" [class.text-green-800]="user.role === 'TECHNICIAN'"
                    >
                      @if (user.role === 'ADMIN') { Administrateur }
                      @if (user.role === 'MANAGER') { Manager }
                      @if (user.role === 'TECHNICIAN') { Technicien }
                    </span>
                  </td>
                  <td class="px-6 py-4">
                    <span class="inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-medium"
                      [class.bg-green-100]="user.status === 'active'" [class.text-green-800]="user.status === 'active'"
                      [class.bg-gray-100]="user.status === 'inactive'" [class.text-gray-800]="user.status === 'inactive'"
                    >
                      <span class="h-2 w-2 rounded-full" [class.bg-green-500]="user.status === 'active'" [class.bg-gray-400]="user.status === 'inactive'"></span>
                      @if (user.status === 'active') { Actif }
                      @if (user.status === 'inactive') { Inactif }
                    </span>
                  </td>
                  <td class="px-6 py-4 text-muted-foreground">{{ user.joinedDate | date:'mediumDate' }}</td>
                  <td class="px-6 py-4 text-right">
                    <button class="rounded-md border px-3 py-1.5 text-xs font-medium hover:bg-muted">
                      Modifier
                    </button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class UsersComponent {
  searchQuery = signal('');
  roleFilter = signal('all');
  statusFilter = signal('all');

  users = signal<User[]>([
    { id: '1', name: 'David Katoh', email: 'david.katoh@ng-fields.com', role: 'ADMIN', status: 'active', joinedDate: new Date('2026-01-15') },
    { id: '2', name: 'Marie Laurent', email: 'marie.laurent@ng-fields.com', role: 'MANAGER', status: 'active', joinedDate: new Date('2026-02-20') },
    { id: '3', name: 'Jean Dupont', email: 'jean.dupont@ng-fields.com', role: 'TECHNICIAN', status: 'active', joinedDate: new Date('2026-03-10') },
    { id: '4', name: 'Sophie Bernard', email: 'sophie.bernard@ng-fields.com', role: 'TECHNICIAN', status: 'active', joinedDate: new Date('2026-03-15') },
    { id: '5', name: 'Pierre Martin', email: 'pierre.martin@ng-fields.com', role: 'TECHNICIAN', status: 'inactive', joinedDate: new Date('2026-04-01') },
    { id: '6', name: 'Amélie Nayo', email: 'amelie.nayo@ng-fields.com', role: 'MANAGER', status: 'active', joinedDate: new Date('2026-03-22') },
    { id: '7', name: 'Kofi Mensah', email: 'kofi.mensah@ng-fields.com', role: 'TECHNICIAN', status: 'active', joinedDate: new Date('2026-04-10') },
  ]);

  filteredUsers = computed(() => {
    let result = this.users();
    const query = this.searchQuery().toLowerCase();
    if (query) {
      result = result.filter(u => u.name.toLowerCase().includes(query) || u.email.toLowerCase().includes(query));
    }
    if (this.roleFilter() !== 'all') {
      result = result.filter(u => u.role === this.roleFilter());
    }
    if (this.statusFilter() !== 'all') {
      result = result.filter(u => u.status === this.statusFilter());
    }
    return result;
  });
}
