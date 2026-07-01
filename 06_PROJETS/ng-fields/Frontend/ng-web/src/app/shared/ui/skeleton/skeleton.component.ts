import { Component } from '@angular/core';

@Component({
  selector: 'span[appSkeleton]',
  standalone: true,
  template: '',
  styleUrl: './skeleton.component.css',
  host: {
    class: 'animate-pulse rounded-md bg-muted',
  },
})
export class SkeletonComponent {}
