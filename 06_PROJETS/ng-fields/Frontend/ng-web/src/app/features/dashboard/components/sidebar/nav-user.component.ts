import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { users } from '../../../../core/navigation/sidebar-items';

@Component({
  selector: 'app-nav-user',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './nav-user.component.html',
  styleUrl: './nav-user.component.css',
})
export class NavUserComponent {
  private auth = inject(AuthService);
  user = users[0];
  isOpen = signal(false);

  menuItems = [
    { id: 'account', label: 'Account', url: '/dashboard/account' },
    { id: 'billing', label: 'Billing', url: '/dashboard/billing' },
    { id: 'notifications', label: 'Notifications', url: '/dashboard/notifications' },
    { id: 'sep1', label: '', separator: true },
    { id: 'logout', label: 'Log out', onClick: () => this.auth.logout() },
  ];

  toggle(): void { this.isOpen.update(v => !v); }
  close(): void { this.isOpen.set(false); }
}
