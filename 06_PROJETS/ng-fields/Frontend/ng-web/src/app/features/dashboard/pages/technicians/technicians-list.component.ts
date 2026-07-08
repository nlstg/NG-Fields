import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Technician } from './components/schemas/technician.schema';
import techniciansData from './components/mocks/technicians-mock.json';
import { TechniciansTableComponent } from './components/technicians-table.component';

@Component({
  selector: 'app-technicians-list',
  standalone: true,
  imports: [CommonModule, TechniciansTableComponent],
  templateUrl: './technicians-list.component.html',
  styleUrl: './technicians-list.component.css',
})
export class TechniciansListComponent implements OnInit {
  technicians = signal<Technician[]>([]);
  isLoading = signal(true);
  error = signal<string | null>(null);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.loadTechnicians();
  }

  private loadTechnicians(): void {
    setTimeout(() => {
      try {
        this.technicians.set(techniciansData as Technician[]);
        this.isLoading.set(false);
      } catch {
        this.error.set('Erreur lors du chargement des techniciens');
        this.isLoading.set(false);
      }
    }, 300);
  }

  onTechnicianClick(technicianId: number): void {
    this.router.navigate(['/dashboard/technicians', technicianId]);
  }

  onCreateTechnician(): void {
    console.log('Créer nouveau technicien');
  }
}
