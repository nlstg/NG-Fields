import { Component } from '@angular/core';

@Component({
  selector: 'label[appLabel]',
  standalone: true,
  template: '<ng-content />',
  styleUrl: './label.component.css',
  host: {
    class: 'text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70',
  },
})
export class LabelComponent {}
