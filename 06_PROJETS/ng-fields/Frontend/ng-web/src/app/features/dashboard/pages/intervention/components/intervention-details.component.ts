import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Intervention } from './schemas/intervention.schema';

@Component({
  selector: 'app-intervention-details',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="text-sm font-medium mb-4">Détails de l'intervention</div>

      <div class="grid gap-4 md:grid-cols-2">
        <div class="space-y-3">
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Contact principal</span>
            <p class="font-medium">{{ intervention.client.contact }}</p>
            <p class="text-xs text-muted-foreground">{{ intervention.client.phone }}</p>
          </div>
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Adresse</span>
            <p class="text-sm">{{ intervention.client.address }}</p>
          </div>
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Email</span>
            <p class="text-sm"><a href="mailto:{{ intervention.client.email }}" class="text-blue-600 hover:underline dark:text-blue-400">{{ intervention.client.email }}</a></p>
          </div>
        </div>

        <div class="space-y-3">
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Technicien assigné</span>
            <div class="flex items-center gap-2 mt-1">
              <img [src]="intervention.technician.avatar" alt="{{ intervention.technician.name }}" class="size-8 rounded-full bg-muted" />
              <div>
                <p class="font-medium text-sm">{{ intervention.technician.name }}</p>
                <p class="text-xs text-muted-foreground">{{ intervention.technician.phone }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="border-t mt-4 pt-4 grid gap-4 md:grid-cols-2">
        <div>
          <span class="text-xs text-muted-foreground uppercase tracking-wide">Durée estimée</span>
          <p class="text-2xl font-bold">{{ intervention.estimatedDuration }} min</p>
        </div>
        @if (intervention.actualDuration !== undefined) {
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Durée réelle</span>
            <p class="text-2xl font-bold">{{ intervention.actualDuration }} min</p>
            @if (intervention.actualDuration < intervention.estimatedDuration) {
              <p class="text-xs text-green-600 dark:text-green-400">
                ✓ {{ intervention.estimatedDuration - intervention.actualDuration }} min de gain
              </p>
            }
            @if (intervention.actualDuration > intervention.estimatedDuration) {
              <p class="text-xs text-red-600 dark:text-red-400">
                ! {{ intervention.actualDuration - intervention.estimatedDuration }} min de dépassement
              </p>
            }
          </div>
        }
      </div>

      <div class="border-t mt-4 pt-4">
        <span class="text-xs text-muted-foreground uppercase tracking-wide block mb-3">Chronologie</span>
        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span>Créée le</span>
            <span class="font-medium">{{ formatDate(intervention.createdAt) }}</span>
          </div>
          <div class="flex justify-between">
            <span>Planifiée le</span>
            <span class="font-medium">{{ formatDate(intervention.scheduledAt) }}</span>
          </div>
          @if (intervention.startedAt) {
            <div class="flex justify-between">
              <span>Commencée le</span>
              <span class="font-medium">{{ formatDate(intervention.startedAt) }}</span>
            </div>
          }
          @if (intervention.completedAt) {
            <div class="flex justify-between">
              <span>Terminée le</span>
              <span class="font-medium">{{ formatDate(intervention.completedAt) }}</span>
            </div>
          }
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class InterventionDetailsComponent {
  @Input() intervention!: Intervention;

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  }
}
