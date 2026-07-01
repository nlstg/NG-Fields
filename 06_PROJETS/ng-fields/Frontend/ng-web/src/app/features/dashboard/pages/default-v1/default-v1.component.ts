import { Component } from '@angular/core';
import { SectionCardsComponent } from './components/section-cards.component';
import { ChartAreaInteractiveComponent } from './components/chart-area-interactive.component';
import { ProposalSectionsTableComponent } from './components/proposal-sections-table/table.component';

@Component({
  selector: 'app-default-v1',
  standalone: true,
  imports: [SectionCardsComponent, ChartAreaInteractiveComponent, ProposalSectionsTableComponent],
  templateUrl: './default-v1.component.html',
  styleUrl: './default-v1.component.css',
})
export class DefaultV1Component {}
