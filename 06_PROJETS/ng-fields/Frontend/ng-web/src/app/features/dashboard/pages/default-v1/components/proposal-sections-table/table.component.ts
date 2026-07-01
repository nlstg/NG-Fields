import { Component, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProposalSectionsRow } from './schema';
import data from './data.json';

@Component({
  selector: 'app-proposal-sections-table',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './table.component.html',
  styleUrl: './table.component.css',
})
export class ProposalSectionsTableComponent {
  allData: ProposalSectionsRow[] = data as ProposalSectionsRow[];

  sortColumn = signal<keyof ProposalSectionsRow>('id');
  sortDirection = signal<'asc' | 'desc'>('asc');
  pageSize = signal(10);
  currentPage = signal(1);
  selectedIds = signal<Set<number>>(new Set());
  visibleColumns = signal<Set<keyof ProposalSectionsRow>>(new Set(['id', 'header', 'type', 'status', 'target', 'limit', 'reviewer']));

  pageSizeOptions = [10, 20, 30, 40, 50];
  allColumns: { key: keyof ProposalSectionsRow; label: string }[] = [
    { key: 'id', label: 'ID' },
    { key: 'header', label: 'Header' },
    { key: 'type', label: 'Type' },
    { key: 'status', label: 'Status' },
    { key: 'target', label: 'Target' },
    { key: 'limit', label: 'Limit' },
    { key: 'reviewer', label: 'Reviewer' },
  ];

  sortedData = computed(() => {
    const col = this.sortColumn();
    const dir = this.sortDirection();
    const sorted = [...this.allData].sort((a, b) => {
      const aVal = a[col];
      const bVal = b[col];
      const cmp = typeof aVal === 'number' && typeof bVal === 'number'
        ? aVal - bVal
        : String(aVal).localeCompare(String(bVal));
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

  totalPages = computed(() => Math.ceil(this.allData.length / this.pageSize()));

  sortBy(column: keyof ProposalSectionsRow): void {
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

  toggleColumn(key: keyof ProposalSectionsRow): void {
    const set = new Set(this.visibleColumns());
    if (set.has(key)) {
      set.delete(key);
    } else {
      set.add(key);
    }
    this.visibleColumns.set(set);
  }

  isColumnVisible(key: keyof ProposalSectionsRow): boolean {
    return this.visibleColumns().has(key);
  }

  get pageInfo(): string {
    const start = (this.currentPage() - 1) * this.pageSize() + 1;
    const end = Math.min(this.currentPage() * this.pageSize(), this.allData.length);
    return `${start}-${end} of ${this.allData.length}`;
  }

  statusClass(status: string): string {
    const map: Record<string, string> = {
      'Approved': 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
      'In Review': 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-400',
      'Pending': 'bg-yellow-100 text-yellow-700 dark:bg-yellow-900/30 dark:text-yellow-400',
      'Rejected': 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400',
      'Draft': 'bg-gray-100 text-gray-700 dark:bg-gray-800 dark:text-gray-400',
    };
    return map[status] || 'bg-gray-100 text-gray-700';
  }
}
