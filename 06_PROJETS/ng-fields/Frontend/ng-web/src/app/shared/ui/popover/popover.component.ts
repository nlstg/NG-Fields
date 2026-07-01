import { Component, Directive, input, output, signal } from '@angular/core';
import { OverlayModule } from '@angular/cdk/overlay';

@Directive({
  selector: '[appPopoverTrigger]',
  standalone: true,
  host: {
    '[attr.aria-haspopup]': '"dialog"',
  },
})
export class PopoverTriggerDirective {
  readonly panelOpen = signal(false);
  readonly toggle = output<boolean>();

  togglePanel(): void {
    this.panelOpen.update(v => !v);
    this.toggle.emit(this.panelOpen());
  }

  open(): void {
    this.panelOpen.set(true);
    this.toggle.emit(true);
  }

  close(): void {
    this.panelOpen.set(false);
    this.toggle.emit(false);
  }
}

@Component({
  selector: '[appPopoverContent]',
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
      <div class="z-50 w-72 rounded-md border bg-popover p-4 text-popover-foreground shadow-md outline-none">
        <ng-content />
      </div>
    </ng-template>
  `,
  styleUrl: './popover.component.css',
})
export class PopoverContentComponent {
  readonly isOpen = input(false);
  readonly align = input<'start' | 'center' | 'end'>('center');

  origin: any;
  positions = [
    { originX: 'start' as const, originY: 'bottom' as const, overlayX: 'start' as const, overlayY: 'top' as const },
    { originX: 'start' as const, originY: 'top' as const, overlayX: 'start' as const, overlayY: 'bottom' as const },
  ];

  close(): void {}
}
