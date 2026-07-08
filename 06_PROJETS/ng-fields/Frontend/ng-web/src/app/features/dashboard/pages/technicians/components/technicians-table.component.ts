import { Component, Input, Output, EventEmitter, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Technician, TechnicianSkill } from './schemas/technician.schema';

@Component({
  selector: 'app-technicians-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './technicians-table.component.html',
  styleUrl: './technicians-table.component.css',
})
export class TechniciansTableComponent {
  @Input() technicians: Technician[] = [];
  @Output() onTechnicianClick = new EventEmitter<number>();

  sortColumn = signal<string>('lastName');
  sortDirection = signal<'asc' | 'desc'>('asc');
  pageSize = signal(10);
  currentPage = signal(1);
  selectedIds = signal<Set<number>>(new Set());
  visibleColumns = signal<Set<string>>(
    new Set(['name', 'skills', 'status', 'thisMonth', 'availability', 'rating'])
  );
  filterStatus = signal<string>('');

  pageSizeOptions = [10, 20, 30, 50];
  allColumns = [
    { key: 'name', label: 'Nom' },
    { key: 'skills', label: 'Compétences' },
    { key: 'status', label: 'Statut' },
    { key: 'thisMonth', label: 'Interventions (mois)' },
    { key: 'availability', label: 'Disponibilité' },
    { key: 'rating', label: 'Évaluation' },
    { key: 'email', label: 'Email' },
    { key: 'phone', label: 'Téléphone' },
    { key: 'hireDate', label: 'Embauché le' },
  ];

  filteredData = computed(() => {
    const status = this.filterStatus();
    return status ? this.technicians.filter(t => t.status === status) : this.technicians;
  });

  sortedData = computed(() => {
    const col = this.sortColumn();
    const dir = this.sortDirection();
    const sorted = [...this.filteredData()].sort((a, b) => {
      let aVal: any = (a as any)[col];
      let bVal: any = (b as any)[col];
      if (col === 'name') {
        aVal = `${a.firstName} ${a.lastName}`;
        bVal = `${b.firstName} ${b.lastName}`;
      }
      if (col === 'thisMonth') {
        aVal = a.interventions.thisMonth;
        bVal = b.interventions.thisMonth;
      }
      const cmp = typeof aVal === 'number' && typeof bVal === 'number'
        ? aVal - bVal
        : String(aVal || '').localeCompare(String(bVal || ''));
      return dir === 'asc' ? cmp : -cmp;
    });
    return sorted;
  });

  paginatedData = computed(() => {
    const page = this.currentPage();
    const size = this.pageSize();
    const start = (page - 1) * size;
    return this.sortedData().slice(start, start + size);
  });

  totalPages = computed(() => Math.ceil(this.sortedData().length / this.pageSize()));

  sortBy(column: string): void {
    if (this.sortColumn() === column) {
      this.sortDirection.set(this.sortDirection() === 'asc' ? 'desc' : 'asc');
    } else {
      this.sortColumn.set(column);
      this.sortDirection.set('asc');
    }
  }

  setPageSize(size: number): void {
    this.pageSize.set(size);
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages()) {
      this.currentPage.set(page);
    }
  }

  toggleAllRows(event: Event): void {
    const checked = (event.target as HTMLInputElement).checked;
    if (checked) {
      this.selectedIds.set(new Set(this.paginatedData().map(r => r.id)));
    } else {
      this.selectedIds.set(new Set());
    }
  }

  toggleRow(id: number): void {
    const set = new Set(this.selectedIds());
    if (set.has(id)) set.delete(id);
    else set.add(id);
    this.selectedIds.set(set);
  }

  isAllSelected(): boolean {
    const page = this.paginatedData();
    return page.length > 0 && page.every(r => this.selectedIds().has(r.id));
  }

  isSelected(id: number): boolean {
    return this.selectedIds().has(id);
  }

  toggleColumn(key: string): void {
    const set = new Set(this.visibleColumns());
    if (set.has(key)) set.delete(key);
    else set.add(key);
    this.visibleColumns.set(set);
  }

  isColumnVisible(key: string): boolean {
    return this.visibleColumns().has(key);
  }

  statusBadgeClass(status: string): string {
    const map: Record<string, string> = {
      'AVAILABLE': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
      'BUSY': 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
      'ON_LEAVE': 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400',
      'INACTIVE': 'bg-gray-100 text-gray-700 dark:bg-gray-900/30 dark:text-gray-400',
    };
    return map[status] || 'bg-gray-100 text-gray-700';
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      'AVAILABLE': 'Disponible',
      'BUSY': 'Occupé',
      'ON_LEAVE': 'En congé',
      'INACTIVE': 'Inactif',
    };
    return map[status] || status;
  }

  skillsDisplay(skills: TechnicianSkill[]): string {
    return skills.slice(0, 2).map(s => s.name).join(', ') + (skills.length > 2 ? '...' : '');
  }

  ratingDisplay(rating: { average: number; count: number }): string {
    return `${rating.average.toFixed(1)}/5 (${rating.count})`;
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  }

  get pageInfo(): string {
    const start = (this.currentPage() - 1) * this.pageSize() + 1;
    const end = Math.min(this.currentPage() * this.pageSize(), this.sortedData().length);
    return `${start}-${end} sur ${this.sortedData().length}`;
  }
}
