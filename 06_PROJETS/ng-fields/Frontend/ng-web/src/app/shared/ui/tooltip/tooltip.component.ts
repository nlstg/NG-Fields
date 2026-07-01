import { Component, Directive, input } from '@angular/core';
import { OverlayModule } from '@angular/cdk/overlay';

@Directive({
  selector: '[appTooltipTrigger]',
  standalone: true,
})
export class TooltipTriggerDirective {}

@Component({
  selector: '[appTooltipContent]',
  standalone: true,
  imports: [OverlayModule],
  template: `
    <ng-template
      cdk-connected-overlay
      [cdkConnectedOverlayOpen]="isOpen()"
      [cdkConnectedOverlayOrigin]="origin"
      [cdkConnectedOverlayPositions]="positions"
      (overlayOutsideClick)="close()"
      (detach)="close()"
    >
      <div class="z-50 overflow-hidden rounded-md border bg-popover px-3 py-1.5 text-sm text-popover-foreground shadow-md">
        <ng-content />
      </div>
    </ng-template>
  `,
  styleUrl: './tooltip.component.css',
})
export class TooltipContentComponent {
  readonly isOpen = input(false);

  origin: any;
  positions = [
    { originX: 'center' as const, originY: 'top' as const, overlayX: 'center' as const, overlayY: 'bottom' as const },
    { originX: 'center' as const, originY: 'bottom' as const, overlayX: 'center' as const, overlayY: 'top' as const },
  ];

  close(): void {}
}
