import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Client } from '../../clients/components/schemas/client.schema';

@Component({
  selector: 'app-client-info',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="rounded-lg border bg-card p-4 md:p-6">
      <div class="text-sm font-medium mb-4">Informations générales</div>

      <div class="grid gap-4 md:grid-cols-2">
        <div class="space-y-3">
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Adresse</span>
            <p class="text-sm mt-1">{{ client.address }}</p>
            <p class="text-sm">{{ client.postalCode }} {{ client.city }}, {{ client.country }}</p>
          </div>
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">Site web</span>
            @if (client.website) {
              <p class="text-sm"><a [href]="client.website" target="_blank" class="text-blue-600 hover:underline">{{ client.website }}</a></p>
            } @else {
              <p class="text-sm text-muted-foreground">—</p>
            }
          </div>
        </div>

        <div class="space-y-3">
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">N° SIRET/RCCM</span>
            <p class="text-sm font-mono">{{ client.registrationNumber }}</p>
          </div>
          <div>
            <span class="text-xs text-muted-foreground uppercase tracking-wide">N° TVA</span>
            <p class="text-sm font-mono">{{ client.taxId }}</p>
          </div>
        </div>
      </div>

      <div class="border-t mt-4 pt-4 grid gap-4 md:grid-cols-3">
        <div>
          <span class="text-xs text-muted-foreground uppercase tracking-wide">Email facturation</span>
          <p class="text-sm mt-1">{{ client.billing.invoiceEmail }}</p>
        </div>
        <div>
          <span class="text-xs text-muted-foreground uppercase tracking-wide">Conditions paiement</span>
          <p class="text-sm mt-1">{{ client.billing.paymentTerms }}</p>
        </div>
        <div>
          <span class="text-xs text-muted-foreground uppercase tracking-wide">Solde</span>
          <p class="text-sm mt-1 font-medium" [class.text-red-600]="client.billing.balance > 0">
            {{ formatCurrency(client.billing.balance) }}
          </p>
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: block; }'],
})
export class ClientInfoComponent {
  @Input() client!: Client;

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'XOF',
    }).format(value);
  }
}
