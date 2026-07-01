import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { sidebarItems } from '../../../../core/navigation/sidebar-items';

@Component({
  selector: 'app-nav-main',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './nav-main.component.html',
  styleUrl: './nav-main.component.css',
})
export class NavMainComponent {
  groups = sidebarItems;
  expanded: Record<string, boolean> = {};

  toggleGroup(id: string): void {
    this.expanded[id] = !this.expanded[id];
  }

  isExpanded(id: string): boolean {
    return this.expanded[id] ?? false;
  }
}
