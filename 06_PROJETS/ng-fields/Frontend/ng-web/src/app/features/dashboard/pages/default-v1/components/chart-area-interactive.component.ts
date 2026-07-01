import { Component, OnInit, OnDestroy, inject, ElementRef, viewChild, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, ChartConfiguration } from 'chart.js/auto';

@Component({
  selector: 'app-chart-area-interactive',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="flex items-center justify-between mb-4">
        <div>
          <div class="text-sm font-medium text-muted-foreground">Total Visitors</div>
          <div class="text-2xl font-bold">{{ totalVisitors }}</div>
        </div>
        <div class="flex gap-1">
          @for (range of timeRanges; track range) {
            <button (click)="setRange(range)" class="rounded-md px-2.5 py-1 text-xs font-medium transition-colors"
              [class.bg-primary]="activeRange() === range"
              [class.text-primary-foreground]="activeRange() === range"
              [class.bg-accent]="activeRange() !== range"
              [class.text-foreground]="activeRange() !== range"
            >{{ range }}</button>
          }
        </div>
      </div>
      <div class="relative">
        <canvas #chartCanvas></canvas>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class ChartAreaInteractiveComponent implements OnInit, OnDestroy {
  private chartRef = viewChild.required<ElementRef<HTMLCanvasElement>>('chartCanvas');
  private chart: Chart | null = null;

  activeRange = signal<string>('30d');
  totalVisitors = '';
  timeRanges = ['7d', '30d', '90d'];

  private allData: { date: string; visitors: number }[] = [];

  constructor() {
    this.generateData();
    this.updateTotal();
  }

  ngOnInit(): void {
    this.createChart();
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
  }

  setRange(range: string): void {
    this.activeRange.set(range);
    this.updateChart();
  }

  private generateData(): void {
    const start = new Date('2024-04-01');
    const end = new Date('2024-06-30');
    const points: { date: string; visitors: number }[] = [];
    let current = new Date(start);
    let base = 800;
    while (current <= end) {
      base += Math.round((Math.random() - 0.45) * 100);
      base = Math.max(400, Math.min(1600, base));
      points.push({
        date: current.toISOString().slice(0, 10),
        visitors: base,
      });
      current.setDate(current.getDate() + 1);
    }
    this.allData = points;
  }

  private getFilteredData() {
    const days = parseInt(this.activeRange().replace('d', ''), 10);
    return this.allData.slice(-days);
  }

  private updateTotal(): void {
    const data = this.getFilteredData();
    this.totalVisitors = data.reduce((s, d) => s + d.visitors, 0).toLocaleString();
  }

  private updateChart(): void {
    if (!this.chart) return;
    const data = this.getFilteredData();
    this.chart.data.labels = data.map(d => d.date.slice(5));
    this.chart.data.datasets[0].data = data.map(d => d.visitors);
    this.chart.update();
    this.updateTotal();
  }

  private createChart(): void {
    const data = this.getFilteredData();
    const canvas = this.chartRef().nativeElement;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0, 'rgba(100, 100, 255, 0.3)');
    gradient.addColorStop(1, 'rgba(100, 100, 255, 0)');

    const config: ChartConfiguration = {
      type: 'line',
      data: {
        labels: data.map(d => d.date.slice(5)),
        datasets: [{
          label: 'Visitors',
          data: data.map(d => d.visitors),
          borderColor: '#6464ff',
          backgroundColor: gradient,
          fill: true,
          tension: 0.4,
          pointRadius: 2,
          pointHoverRadius: 5,
          borderWidth: 2,
        }],
      },
      options: {
        responsive: true,
        maintainAspectRatio: true,
        aspectRatio: 3,
        plugins: {
          legend: { display: false },
          tooltip: {
            backgroundColor: 'hsl(var(--popover))',
            titleColor: 'hsl(var(--popover-foreground))',
            bodyColor: 'hsl(var(--popover-foreground))',
            borderColor: 'hsl(var(--border))',
            borderWidth: 1,
            padding: 10,
            displayColors: false,
          },
        },
        scales: {
          x: {
            grid: { display: false },
            ticks: { color: 'hsl(var(--muted-foreground))', maxTicksLimit: 8 },
          },
          y: {
            grid: { color: 'hsl(var(--border))' },
            ticks: { color: 'hsl(var(--muted-foreground))' },
            beginAtZero: false,
          },
        },
        interaction: {
          intersect: false,
          mode: 'index',
        },
      },
    };

    this.chart = new Chart(ctx, config);
    this.updateTotal();
  }
}
