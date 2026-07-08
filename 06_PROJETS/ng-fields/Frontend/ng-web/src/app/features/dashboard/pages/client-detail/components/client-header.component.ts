import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Client } from '../../clients/components/schemas/client.schema';

@Component({
  selector: 'app-client-header',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="flex items-start justify-between gap-4 mb-4">
        <div>
          <h1 class="text-3xl font-bold">{{ client.name }}</h1>
          <p class="text-sm text-muted-foreground mt-1">{{ client.type }}</p>
        </div>
        <span class="inline-block rounded-full px-3 py-1 text-xs font-medium {{ statusBadgeClass() }}">
          {{ statusLabel() }}
        </span>
      </div>

      <div class="grid gap-4 md:grid-cols-5 text-sm">
        <div>
          <span class="text-muted-foreground">Email</span>
          <p class="font-medium"><a href="mailto:{{ client.email }}" class="text-blue-600 hover:underline">{{ client.email }}</a></p>
        </div>
        <div>
          <span class="text-muted-foreground">Téléphone</span>
          <p class="font-medium"><a href="tel:{{ client.phone }}" class="text-blue-600 hover:underline">{{ client.phone }}</a></p>
        </div>
        <div>
          <span class="text-muted-foreground">Ville</span>
          <p class="font-medium">{{ client.city }}</p>
        </div>
        <div>
          <span class="text-muted-foreground">Client depuis</span>
          <p class="font-medium">{{ formatDate(client.createdAt) }}</p>
        </div>
        <div>
          <span class="text-muted-foreground">Interventions</span>
          <p class="font-medium">{{ client.interventions.total }}</p>
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class ClientHeaderComponent {
  @Input() client!: Client;
  @Output() onStatusChange = new EventEmitter<string>();

  statusLabel(): string {
    const map: Record<string, string> = {
      'ACTIVE': 'Actif',
      'INACTIVE': 'Inactif',
      'SUSPENDED': 'Suspendu',
    };
    return map[this.client.status] || 'Inconnu';
  }

  statusBadgeClass(): string {
    const map: Record<string, string> = {
      'ACTIVE': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
      'INACTIVE': 'bg-gray-100 text-gray-700 dark:bg-gray-900/30 dark:text-gray-400',
      'SUSPENDED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
    };
    return map[this.client.status] || 'bg-gray-100 text-gray-700';
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  }
}
