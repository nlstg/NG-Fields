import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TimelineEntry } from './schemas/intervention.schema';

@Component({
  selector: 'app-intervention-timeline',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="text-sm font-medium mb-4">Historique</div>

      <div class="space-y-4">
        @for (entry of entries; track entry.id) {
          <div class="flex gap-3 pb-4 border-b last:border-b-0">
            <div class="flex flex-col items-center gap-2">
              <div class="size-8 rounded-full {{ dotClass(entry.type) }} flex items-center justify-center">
                @switch (entry.type) {
                  @case ('status_change') {
                    <svg class="size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="20 6 9 17 4 12"/></svg>
                  }
                  @case ('note_added') {
                    <svg class="size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>
                  }
                  @case ('assigned') {
                    <svg class="size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
                  }
                  @case ('file_added') {
                    <svg class="size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l9.19-9.19a4 4 0 0 1 5.66 5.66l-9.2 9.19a2 2 0 0 1-2.83-2.83l8.49-8.48"/></svg>
                  }
                  @case ('duration_updated') {
                    <svg class="size-4" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
                  }
                }
              </div>
            </div>

            <div class="flex-1 min-w-0">
              <div class="text-xs font-medium text-muted-foreground">{{ timelineLabel(entry.type) }}</div>
              <p class="text-sm mt-0.5">{{ entry.detail }}</p>
              <div class="flex items-center justify-between mt-1">
                <span class="text-xs text-muted-foreground">{{ entry.actor }}</span>
                <span class="text-xs text-muted-foreground">{{ formatTime(entry.timestamp) }}</span>
              </div>
            </div>
          </div>
        }
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class InterventionTimelineComponent {
  @Input() entries: TimelineEntry[] = [];

  dotClass(type: string): string {
    const map: Record<string, string> = {
      'status_change': 'bg-blue-100 text-blue-700 dark:bg-blue-900/50',
      'note_added': 'bg-green-100 text-green-700 dark:bg-green-900/50',
      'assigned': 'bg-purple-100 text-purple-700 dark:bg-purple-900/50',
      'file_added': 'bg-orange-100 text-orange-700 dark:bg-orange-900/50',
      'duration_updated': 'bg-cyan-100 text-cyan-700 dark:bg-cyan-900/50',
    };
    return map[type] || 'bg-gray-100 text-gray-700';
  }

  timelineLabel(type: string): string {
    const map: Record<string, string> = {
      'status_change': 'Changement de statut',
      'note_added': 'Note ajoutée',
      'assigned': 'Assignation',
      'file_added': 'Fichier ajouté',
      'duration_updated': 'Durée mise à jour',
    };
    return map[type] || 'Événement';
  }

  formatTime(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
