import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientContact } from '../../clients/components/schemas/client.schema';

@Component({
  selector: 'app-client-contacts',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="text-sm font-medium mb-4">Contacts</div>

      @if (contacts.length === 0) {
        <p class="text-xs text-muted-foreground">Aucun contact.</p>
      } @else {
        <div class="space-y-3">
          @for (contact of contacts; track contact.name) {
            <div class="rounded-md border p-3">
              <p class="font-medium text-sm">{{ contact.name }}</p>
              <p class="text-xs text-muted-foreground">{{ contact.title }}</p>
              <div class="mt-2 space-y-1 text-xs">
                <p>
                  <a [href]="'mailto:' + contact.email" class="text-blue-600 hover:underline">{{ contact.email }}</a>
                </p>
                <p>
                  <a [href]="'tel:' + contact.phone" class="text-blue-600 hover:underline">{{ contact.phone }}</a>
                </p>
                @if (contact.mobile && contact.mobile !== contact.phone) {
                  <p>
                    <span class="text-muted-foreground">Mobile : </span>
                    <a [href]="'tel:' + contact.mobile" class="text-blue-600 hover:underline">{{ contact.mobile }}</a>
                  </p>
                }
              </div>
            </div>
          }
        </div>
      }
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class ClientContactsComponent {
  @Input() contacts: ClientContact[] = [];
}
