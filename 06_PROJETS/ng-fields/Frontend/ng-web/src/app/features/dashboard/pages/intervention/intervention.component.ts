import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { Intervention } from './components/schemas/intervention.schema';
import mockData from './components/mocks/intervention-mock.json';
import { InterventionHeaderComponent } from './components/intervention-header.component';
import { InterventionDetailsComponent } from './components/intervention-details.component';
import { InterventionTimelineComponent } from './components/intervention-timeline.component';
import { InterventionNotesComponent } from './components/intervention-notes.component';
import { InterventionFilesComponent } from './components/intervention-files.component';
import { InterventionActionsComponent } from './components/intervention-actions.component';

@Component({
  selector: 'app-intervention',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    InterventionHeaderComponent,
    InterventionDetailsComponent,
    InterventionTimelineComponent,
    InterventionNotesComponent,
    InterventionFilesComponent,
    InterventionActionsComponent,
  ],
  templateUrl: './intervention.component.html',
  styleUrl: './intervention.component.css',
})
export class InterventionComponent implements OnInit {
  intervention = signal<Intervention | null>(null);
  isLoading = signal(true);
  error = signal<string | null>(null);

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      this.loadIntervention(id);
    });
  }

  private loadIntervention(id: string | null): void {
    if (!id) {
      this.error.set('ID intervention manquant');
      this.isLoading.set(false);
      return;
    }
    setTimeout(() => {
      try {
        this.intervention.set(mockData as unknown as Intervention);
        this.isLoading.set(false);
      } catch {
        this.error.set("Erreur lors du chargement de l'intervention");
        this.isLoading.set(false);
      }
    }, 300);
  }

  onStatusChange(newStatus: string): void {
    const current = this.intervention();
    if (!current) return;
    current.status = newStatus as Intervention['status'];
    this.intervention.set({ ...current });
  }

  onAssign(technicianId: number): void {
    const current = this.intervention();
    if (!current) return;
    current.technician.id = technicianId;
    this.intervention.set({ ...current });
  }

  onNoteAdded(text: string): void {
    const current = this.intervention();
    if (!current) return;
    current.notes = text;
    current.timeline.push({
      id: current.timeline.length + 1,
      type: 'note_added',
      timestamp: new Date().toISOString(),
      actor: 'Current User',
      detail: text,
    });
    this.intervention.set({ ...current });
  }
}
