import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { AppSidebarComponent } from './components/sidebar/app-sidebar.component';
import { SearchDialogComponent } from './components/header/search-dialog.component';
import { ThemeSwitcherComponent } from './components/header/theme-switcher.component';
import { LayoutControlsComponent } from './components/header/layout-controls.component';
import { AccountSwitcherComponent } from './components/header/account-switcher.component';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, AppSidebarComponent, SearchDialogComponent, ThemeSwitcherComponent, LayoutControlsComponent, AccountSwitcherComponent],
  templateUrl: './dashboard-layout.component.html',
  styleUrl: './dashboard-layout.component.css',
})
export class DashboardLayoutComponent {}
