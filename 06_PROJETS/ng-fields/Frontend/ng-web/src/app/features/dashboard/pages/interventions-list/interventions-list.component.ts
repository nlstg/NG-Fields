import { Component } from '@angular/core';
import { ProposalSectionsTableComponent } from '../overview/components/proposal-sections-table/table.component';

@Component({
  selector: 'app-interventions-list',
  standalone: true,
  imports: [ProposalSectionsTableComponent],
  template: `<app-proposal-sections-table></app-proposal-sections-table>`,
  styles: [':host { display: block; }'],
})
export class InterventionsListComponent {}
