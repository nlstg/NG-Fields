import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Client } from './components/schemas/client.schema';
import clientsData from './components/mocks/clients-mock.json';
import { ClientsTableComponent } from './components/clients-table.component';

@Component({
  selector: 'app-clients-list',
  standalone: true,
  imports: [CommonModule, ClientsTableComponent],
  templateUrl: './clients-list.component.html',
  styleUrl: './clients-list.component.css',
})
export class ClientsListComponent implements OnInit {
  clients = signal<Client[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadClients();
  }

  private loadClients(): void {
    setTimeout(() => {
      try {
        this.clients.set(clientsData as Client[]);
        this.isLoading.set(false);
      } catch {
        this.error.set('Erreur lors du chargement des clients');
        this.isLoading.set(false);
      }
    }, 300);
  }

  onClientClick(clientId: number): void {
    this.router.navigate(['/dashboard/clients', clientId]);
  }

  onCreateClient(): void {
    console.log('Créer nouveau client');
  }
}
