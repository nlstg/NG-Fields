import { Component, Directive } from '@angular/core';

@Directive({
  selector: 'img[appAvatarImage]',
  standalone: true,
  host: {
    class: 'aspect-square h-full w-full',
  },
})
export class AvatarImageDirective {}

@Component({
  selector: 'span[appAvatarFallback]',
  standalone: true,
  template: '<ng-content />',
  styleUrl: './avatar.component.css',
  host: {
    class: 'flex h-full w-full items-center justify-center rounded-full bg-muted text-xs font-medium',
  },
})
export class AvatarFallbackComponent {}

@Component({
  selector: 'span[appAvatar]',
  standalone: true,
  template: `
    <ng-content />
  `,
  styleUrl: './avatar.component.css',
  host: {
    class: 'relative flex h-10 w-10 shrink-0 overflow-hidden rounded-full',
  },
})
export class AvatarComponent {}
