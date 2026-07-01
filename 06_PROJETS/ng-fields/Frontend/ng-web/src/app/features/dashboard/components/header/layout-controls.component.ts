import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThemeService, ThemeMode, ThemePreset } from '../../../../core/theme/theme.service';

@Component({
  selector: 'app-layout-controls',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './layout-controls.component.html',
  styleUrl: './layout-controls.component.css',
})
export class LayoutControlsComponent {
  private theme = inject(ThemeService);
  isOpen = signal(false);

  presets: ThemePreset[] = ['default', 'brutalist', 'soft-pop', 'tangerine'];
  modes: { value: ThemeMode; label: string }[] = [
    { value: 'light', label: 'Light' },
    { value: 'dark', label: 'Dark' },
    { value: 'system', label: 'System' },
  ];

  fonts = [
    { value: 'system-ui, sans-serif', label: 'System' },
    { value: 'Inter, sans-serif', label: 'Inter' },
    { value: 'Georgia, serif', label: 'Serif' },
    { value: 'monospace', label: 'Mono' },
  ];

  get currentMode(): ThemeMode { return this.theme.currentMode; }
  get currentPreset(): ThemePreset { return this.theme.currentPreset; }

  setMode(mode: ThemeMode): void { this.theme.setThemeMode(mode); }
  setPreset(preset: ThemePreset): void { this.theme.setThemePreset(preset); }

  setFont(font: string): void {
    document.documentElement.style.fontFamily = font;
  }

  toggle(): void { this.isOpen.update(v => !v); }
  close(): void { this.isOpen.set(false); }
}
