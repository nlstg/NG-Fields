import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Client } from '../../clients/components/schemas/client.schema';

@Component({
  selector: 'app-client-actions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="text-sm font-medium mb-4">Actions</div>

      <div class="space-y-2">
        <button
          class="w-full rounded-md bg-blue-100 px-3 py-2 text-xs font-medium text-blue-700 hover:bg-blue-200 dark:bg-blue-900/30 dark:text-blue-400 dark:hover:bg-blue-900/50"
        >
          + Nouvelle intervention
        </button>

        @if (client.status !== 'SUSPENDED') {
          <button
            (click)="showStatusSelect = !showStatusSelect"
            class="w-full rounded-md bg-purple-100 px-3 py-2 text-xs font-medium text-purple-700 hover:bg-purple-200 dark:bg-purple-900/30 dark:text-purple-400 dark:hover:bg-purple-900/50"
          >
            Changer le statut
          </button>
          @if (showStatusSelect) {
            <select
              [(ngModel)]="selectedStatus"
              (change)="changeStatus()"
              class="w-full rounded-md border bg-background p-2 text-xs"
            >
              <option value="">Sélectionner un statut...</option>
              <option value="ACTIVE">Actif</option>
              <option value="INACTIVE">Inactif</option>
              <option value="SUSPENDED">Suspendu</option>
            </select>
          }
        }

        <button
          class="w-full rounded-md bg-green-100 px-3 py-2 text-xs font-medium text-green-700 hover:bg-green-200 dark:bg-green-900/30 dark:text-green-400 dark:hover:bg-green-900/50"
        >
          Éditer les informations
        </button>

        <button
          class="w-full rounded-md bg-gray-100 px-3 py-2 text-xs font-medium text-gray-700 hover:bg-gray-200 dark:bg-gray-900/30 dark:text-gray-400 dark:hover:bg-gray-900/50"
        >
          Télécharger historique
        </button>

        <button
          class="w-full rounded-md bg-red-100 px-3 py-2 text-xs font-medium text-red-700 hover:bg-red-200 dark:bg-red-900/30 dark:text-red-400 dark:hover:bg-red-900/50"
        >
          Supprimer le client
        </button>
      </div>

      <div class="mt-4 rounded-md bg-muted p-2 text-xs text-muted-foreground">
        <p>Status : <strong>{{ statusLabel() }}</strong></p>
        <p>Interventions : <strong>{{ client.interventions.total }}</strong> ({{ client.interventions.resolved }} résolues)</p>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class ClientActionsComponent {
  @Input() client!: Client;
  @Output() onStatusChange = new EventEmitter<string>();

  showStatusSelect = false;
  selectedStatus = '';

  statusLabel(): string {
    const map: Record<string, string> = {
      'ACTIVE': 'Actif',
      'INACTIVE': 'Inactif',
      'SUSPENDED': 'Suspendu',
    };
    return map[this.client.status] || 'Inconnu';
  }

  changeStatus(): void {
    if (this.selectedStatus) {
      this.onStatusChange.emit(this.selectedStatus);
      this.showStatusSelect = false;
      this.selectedStatus = '';
    }
  }
}
