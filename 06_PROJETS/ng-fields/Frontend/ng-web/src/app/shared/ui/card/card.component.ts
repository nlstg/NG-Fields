import { Directive, Component } from '@angular/core';

@Directive({
  selector: '[appCard]',
  standalone: true,
  host: {
    class: 'rounded-xl border bg-card text-card-foreground shadow-xs',
  },
})
export class CardDirective {}

@Directive({
  selector: '[appCardHeader]',
  standalone: true,
  host: {
    class: 'flex flex-col gap-1.5 p-6',
  },
})
export class CardHeaderDirective {}

@Directive({
  selector: '[appCardTitle]',
  standalone: true,
  host: {
    class: 'font-semibold leading-none tracking-tight',
  },
})
export class CardTitleDirective {}

@Directive({
  selector: '[appCardDescription]',
  standalone: true,
  host: {
    class: 'text-sm text-muted-foreground',
  },
})
export class CardDescriptionDirective {}

@Directive({
  selector: '[appCardContent]',
  standalone: true,
  host: {
    class: 'p-6 pt-0',
  },
})
export class CardContentDirective {}

@Directive({
  selector: '[appCardFooter]',
  standalone: true,
  host: {
    class: 'flex items-center p-6 pt-0',
  },
})
export class CardFooterDirective {}

@Directive({
  selector: '[appCardAction]',
  standalone: true,
  host: {
    class: 'ml-auto flex items-center gap-2',
  },
})
export class CardActionDirective {}
