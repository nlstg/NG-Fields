import { Component, input, HostBinding } from '@angular/core';

@Component({
  selector: 'span[appSeparator]',
  standalone: true,
  template: '',
  styleUrl: './separator.component.css',
})
export class SeparatorComponent {
  readonly orientation = input<'horizontal' | 'vertical'>('horizontal');

  @HostBinding('class')
  get hostClass(): string {
    return `shrink-0 bg-border ${this.orientation() === 'vertical' ? 'h-full w-px' : 'h-px w-full'}`;
  }
}
