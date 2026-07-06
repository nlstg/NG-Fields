import { Component, inject, signal, HostListener, ElementRef } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../../core/auth/auth.service';
import { users } from '../../../../core/navigation/sidebar-items';
import { IconComponent } from '../../../../shared/ui/icon/icon.component';
import { SidebarService } from '../../../../core/sidebar/sidebar.service';

@Component({
  selector: 'app-nav-user',
  standalone: true,
  imports: [RouterModule, IconComponent],
  templateUrl: './nav-user.component.html',
  styleUrl: './nav-user.component.css',
})
export class NavUserComponent {
  private auth = inject(AuthService);
  sidebar = inject(SidebarService);
  private el = inject(ElementRef);
  user = users[0];
  isOpen = signal(false);

  menuItems = [
    { id: 'account', label: 'Account', url: '/dashboard/account', icon: 'circle-user' },
    { id: 'billing', label: 'Billing', url: '/dashboard/billing', icon: 'credit-card' },
    { id: 'notifications', label: 'Notifications', url: '/dashboard/notifications', icon: 'message-square-dot' },
    { id: 'sep1', label: '', separator: true },
    { id: 'logout', label: 'Log out', icon: 'log-out', onClick: () => this.auth.logout() },
  ];

  getInitials(name: string): string {
    return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
  }

  toggle(): void {
    this.isOpen.update(v => !v);
  }

  close(): void {
    this.isOpen.set(false);
  }

  @HostListener('document:mousedown', ['$event'])
  onGlobalClick(event: MouseEvent): void {
    if (!this.el.nativeElement.contains(event.target)) {
      this.isOpen.set(false);
    }
  }
}
