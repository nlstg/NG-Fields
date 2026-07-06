import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export type ThemeMode = 'light' | 'dark' | 'system';
export type ThemePreset = 'default';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private renderer: Renderer2;
  private themeMode = new BehaviorSubject<ThemeMode>('light');
  private themePreset = new BehaviorSubject<ThemePreset>('default');

  themeMode$: Observable<ThemeMode> = this.themeMode.asObservable();
  themePreset$: Observable<ThemePreset> = this.themePreset.asObservable();

  constructor(rendererFactory: RendererFactory2) {
    this.renderer = rendererFactory.createRenderer(null, null);
    this.initFromCookies();
  }

  private initFromCookies(): void {
    const mode = this.readCookie('ng-stars-theme_mode') as ThemeMode | null;
    const preset = this.readCookie('ng-stars-theme_preset') as ThemePreset | null;
    if (mode) this.setThemeMode(mode);
    if (preset) this.setThemePreset(preset);
  }

  private readCookie(name: string): string | null {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
  }

  private writeCookie(name: string, value: string): void {
    document.cookie = `${name}=${value}; path=/; max-age=${60 * 60 * 24 * 365}`;
  }

  setThemeMode(mode: ThemeMode): void {
    this.themeMode.next(mode);
    const root = document.documentElement;
    this.renderer.setAttribute(root, 'data-theme-mode', mode);
    let resolved = mode;
    if (mode === 'system') {
      resolved = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }
    if (resolved === 'dark') {
      this.renderer.addClass(root, 'dark');
    } else {
      this.renderer.removeClass(root, 'dark');
    }
    root.style.colorScheme = resolved === 'dark' ? 'dark' : 'light';
    this.writeCookie('ng-stars-theme_mode', mode);
  }

  setThemePreset(preset: ThemePreset): void {
    this.themePreset.next(preset);
    this.renderer.setAttribute(document.documentElement, 'data-theme-preset', preset);
    this.writeCookie('ng-stars-theme_preset', preset);
  }

  cycleTheme(): void {
    const cycle: ThemeMode[] = ['light', 'dark', 'system'];
    const current = this.themeMode.value;
    const idx = cycle.indexOf(current);
    this.setThemeMode(cycle[(idx + 1) % cycle.length]);
  }

  get currentMode(): ThemeMode {
    return this.themeMode.value;
  }

  get currentPreset(): ThemePreset {
    return this.themePreset.value;
  }
}
