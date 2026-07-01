import { Directive } from '@angular/core';

@Directive({
  selector: 'table[appTable]',
  standalone: true,
  host: {
    class: 'w-full caption-bottom text-sm',
  },
})
export class TableComponent {}

@Directive({
  selector: 'thead[appTableHeader]',
  standalone: true,
  host: {
    class: '[&_tr]:border-b',
  },
})
export class TableHeaderComponent {}

@Directive({
  selector: 'tbody[appTableBody]',
  standalone: true,
  host: {
    class: '[&_tr:last-child]:border-0',
  },
})
export class TableBodyComponent {}

@Directive({
  selector: 'tr[appTableRow]',
  standalone: true,
  host: {
    class: 'border-b transition-colors hover:bg-muted/50 data-[state=selected]:bg-muted',
  },
})
export class TableRowComponent {}

@Directive({
  selector: 'th[appTableHead]',
  standalone: true,
  host: {
    class: 'h-12 px-4 text-left align-middle font-medium text-muted-foreground [&:has([role=checkbox])]:pr-0',
  },
})
export class TableHeadComponent {}

@Directive({
  selector: 'td[appTableCell]',
  standalone: true,
  host: {
    class: 'p-4 align-middle [&:has([role=checkbox])]:pr-0',
  },
})
export class TableCellComponent {}
