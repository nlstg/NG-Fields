import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { users } from '../../../../core/navigation/sidebar-items';

@Component({
  selector: 'app-account-switcher',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './account-switcher.component.html',
  styleUrl: './account-switcher.component.css',
})
export class AccountSwitcherComponent {
  private auth = inject(AuthService);
  user = users[0];
  isOpen = signal(false);

  menuItems = [
    { id: 'profile', label: 'Profile', url: '/dashboard/profile' },
    { id: 'settings', label: 'Account Settings', url: '/dashboard/settings' },
    { id: 'billing', label: 'Billing', url: '/dashboard/billing' },
    { id: 'sep1', label: '', separator: true },
    { id: 'logout', label: 'Log out', onClick: () => this.auth.logout() },
  ];

  toggle(): void { this.isOpen.update(v => !v); }
  close(): void { this.isOpen.set(false); }
  handleAction(item: { onClick?: () => void; url?: string }): void {
    this.isOpen.set(false);
    item.onClick?.();
  }
}
