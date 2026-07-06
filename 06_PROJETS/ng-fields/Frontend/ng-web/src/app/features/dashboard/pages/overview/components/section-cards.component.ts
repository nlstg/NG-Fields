import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface StatCard {
  title: string;
  value: string;
  badge: string;
  badgeVariant: 'positive' | 'negative' | 'neutral';
  description: string;
  icon: string;
}

@Component({
  selector: 'app-section-cards',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
      @for (card of cards; track card.title) {
        <div class="rounded-lg border bg-card p-4 md:p-6">
          <div class="flex items-center gap-2">
            <span [innerHTML]="card.icon" class="size-4 text-muted-foreground"></span>
            <span class="text-xs font-medium text-muted-foreground uppercase tracking-wider">{{ card.title }}</span>
          </div>
          <div class="mt-3 flex items-baseline gap-2">
            <span class="text-2xl font-bold">{{ card.value }}</span>
            <span class="rounded-md px-1.5 py-0.5 text-xs font-medium"
              [class.bg-green-100]="card.badgeVariant === 'positive'"
              [class.text-green-700]="card.badgeVariant === 'positive'"
              [class.bg-red-100]="card.badgeVariant === 'negative'"
              [class.text-red-700]="card.badgeVariant === 'negative'"
              [class.bg-blue-100]="card.badgeVariant === 'neutral'"
              [class.text-blue-700]="card.badgeVariant === 'neutral'"
            >{{ card.badge }}</span>
          </div>
          <p class="mt-1 text-xs text-muted-foreground">{{ card.description }}</p>
        </div>
      }
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class SectionCardsComponent {
  cards: StatCard[] = [
    {
      title: 'Total Revenue',
      value: '$1,250.00',
      badge: '+12.5%',
      badgeVariant: 'positive',
      description: 'vs last month',
      icon: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>',
    },
    {
      title: 'New Customers',
      value: '1,234',
      badge: '-20%',
      badgeVariant: 'negative',
      description: 'vs last month',
      icon: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>',
    },
    {
      title: 'Active Accounts',
      value: '45,678',
      badge: '+12.5%',
      badgeVariant: 'positive',
      description: 'vs last month',
      icon: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>',
    },
    {
      title: 'Growth Rate',
      value: '4.5%',
      badge: '+4.5%',
      badgeVariant: 'positive',
      description: 'vs last month',
      icon: '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>',
    },
  ];
}
