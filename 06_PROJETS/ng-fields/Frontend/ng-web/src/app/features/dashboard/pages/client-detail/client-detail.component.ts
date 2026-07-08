import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { RouterModule } from '@angular/router';
import { ClientDetailView } from '../clients/components/schemas/client.schema';
import mockDetailData from './components/mocks/client-detail-mock.json';
import { ClientHeaderComponent } from './components/client-header.component';
import { ClientInfoComponent } from './components/client-info.component';
import { ClientInterventionsHistoryComponent } from './components/client-interventions-history.component';
import { ClientInterventionsUpcomingComponent } from './components/client-interventions-upcoming.component';
import { ClientContactsComponent } from './components/client-contacts.component';
import { ClientNotesComponent } from './components/client-notes.component';
import { ClientActionsComponent } from './components/client-actions.component';

@Component({
  selector: 'app-client-detail',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ClientHeaderComponent,
    ClientInfoComponent,
    ClientInterventionsHistoryComponent,
    ClientInterventionsUpcomingComponent,
    ClientContactsComponent,
    ClientNotesComponent,
    ClientActionsComponent,
  ],
  templateUrl: './client-detail.component.html',
  styleUrl: './client-detail.component.css',
})
export class ClientDetailComponent implements OnInit {
  client = signal<ClientDetailView | null>(null);
  isLoading = signal(true);
  error = signal<string | null>(null);

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      this.loadClient(id);
    });
  }

  private loadClient(id: string | null): void {
    if (!id) {
      this.error.set('ID client manquant');
      this.isLoading.set(false);
      return;
    }

    setTimeout(() => {
      try {
        this.client.set(mockDetailData as unknown as ClientDetailView);
        this.isLoading.set(false);
      } catch {
        this.error.set('Erreur lors du chargement du client');
        this.isLoading.set(false);
      }
    }, 300);
  }

  onStatusChange(newStatus: string): void {
    const current = this.client();
    if (!current) return;
    current.status = newStatus as any;
    this.client.set({ ...current });
  }

  onNotesUpdate(newNotes: string): void {
    const current = this.client();
    if (!current) return;
    current.notes = newNotes;
    this.client.set({ ...current });
  }
}
