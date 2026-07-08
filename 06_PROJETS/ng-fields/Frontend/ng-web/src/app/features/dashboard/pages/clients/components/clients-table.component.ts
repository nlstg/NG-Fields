import { Component, Input, Output, EventEmitter, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Client } from './schemas/client.schema';

@Component({
  selector: 'app-clients-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clients-table.component.html',
  styleUrl: './clients-table.component.css',
})
export class ClientsTableComponent {
  @Input() clients: Client[] = [];
  @Output() onClientClick = new EventEmitter<number>();

  sortColumn = signal<keyof Client>('name');
  sortDirection = signal<'asc' | 'desc'>('asc');
  pageSize = signal(10);
  currentPage = signal(1);
  selectedIds = signal<Set<number>>(new Set());
  visibleColumns = signal<Set<keyof Client>>(
    new Set(['name', 'type', 'status', 'contact', 'email', 'phone', 'interventions'])
  );
  filterStatus = signal<string>('');

  pageSizeOptions = [10, 20, 30, 50];
  allColumns: { key: keyof Client; label: string }[] = [
    { key: 'name', label: 'Nom' },
    { key: 'type', label: 'Type' },
    { key: 'status', label: 'Statut' },
    { key: 'contact', label: 'Contact principal' },
    { key: 'email', label: 'Email' },
    { key: 'phone', label: 'Téléphone' },
    { key: 'city', label: 'Ville' },
    { key: 'createdAt', label: 'Créé le' },
    { key: 'lastInterventionAt', label: 'Dernière intervention' },
    { key: 'interventions', label: 'Interventions' },
  ];

  filteredData = computed(() => {
    const status = this.filterStatus();
    return status
      ? this.clients.filter(c => c.status === status)
      : this.clients;
  });

  sortedData = computed(() => {
    const col = this.sortColumn();
    const dir = this.sortDirection();
    const sorted = [...this.filteredData()].sort((a, b) => {
      let aVal = a[col];
      let bVal = b[col];
      if (col === 'contact') {
        aVal = (aVal as any)?.name || '';
        bVal = (bVal as any)?.name || '';
      }
      if (col === 'interventions') {
        aVal = (aVal as any)?.total || 0;
        bVal = (bVal as any)?.total || 0;
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

  sortBy(column: keyof Client): void {
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
    if (set.has(id)) {
      set.delete(id);
    } else {
      set.add(id);
    }
    this.selectedIds.set(set);
  }

  isAllSelected(): boolean {
    const page = this.paginatedData();
    return page.length > 0 && page.every(r => this.selectedIds().has(r.id));
  }

  isSelected(id: number): boolean {
    return this.selectedIds().has(id);
  }

  toggleColumn(key: keyof Client): void {
    const set = new Set(this.visibleColumns());
    if (set.has(key)) {
      set.delete(key);
    } else {
      set.add(key);
    }
    this.visibleColumns.set(set);
  }

  isColumnVisible(key: keyof Client): boolean {
    return this.visibleColumns().has(key);
  }

  statusBadgeClass(status: string): string {
    const map: Record<string, string> = {
      'ACTIVE': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
      'INACTIVE': 'bg-gray-100 text-gray-700 dark:bg-gray-900/30 dark:text-gray-400',
      'SUSPENDED': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
    };
    return map[status] || 'bg-gray-100 text-gray-700';
  }

  statusLabel(status: string): string {
    const map: Record<string, string> = {
      'ACTIVE': 'Actif',
      'INACTIVE': 'Inactif',
      'SUSPENDED': 'Suspendu',
    };
    return map[status] || status;
  }

  formatDate(date: string | undefined): string {
    if (!date) return '—';
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
