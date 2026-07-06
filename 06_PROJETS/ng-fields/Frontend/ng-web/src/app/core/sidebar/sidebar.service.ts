import { Injectable, signal, computed, effect, WritableSignal } from '@angular/core';

export type SidebarLayoutMode = 'sidebar' | 'inset' | 'floating';
export type SidebarCollapsibleMode = 'icon' | 'offcanvas' | 'none';

const SIDEBAR_COOKIE = 'sidebar_state';
const LAYOUT_MODE_COOKIE = 'sidebar_layout_mode';
const COLLAPSIBLE_MODE_COOKIE = 'sidebar_collapsible_mode';
const SIDEBAR_WIDTH = '16rem';
const SIDEBAR_WIDTH_ICON = '3rem';

@Injectable({ providedIn: 'root' })
export class SidebarService {
  sidebarOpen: WritableSignal<boolean>;
  layoutMode: WritableSignal<SidebarLayoutMode>;
  collapsibleMode: WritableSignal<SidebarCollapsibleMode>;
  isMobile = signal(false);
  sidebarWidth = computed(() => this.sidebarOpen() ? SIDEBAR_WIDTH : SIDEBAR_WIDTH_ICON);

  private mediaQuery: MediaQueryList | null = null;

  constructor() {
    const saved = this.readCookie(SIDEBAR_COOKIE);
    this.sidebarOpen = signal(saved !== 'false');
    this.layoutMode = signal(this.readCookie(LAYOUT_MODE_COOKIE) as SidebarLayoutMode || 'sidebar');
    this.collapsibleMode = signal(this.readCookie(COLLAPSIBLE_MODE_COOKIE) as SidebarCollapsibleMode || 'icon');

    if (typeof window !== 'undefined') {
      this.mediaQuery = window.matchMedia('(max-width: 767px)');
      this.isMobile.set(this.mediaQuery.matches);
      this.mediaQuery.addEventListener('change', (e) => this.isMobile.set(e.matches));
    }

    effect(() => {
      this.writeCookie(SIDEBAR_COOKIE, this.sidebarOpen() ? 'true' : 'false', 7);
    });

    effect(() => {
      document.documentElement.dataset['sidebarVariant'] = this.layoutMode();
      document.documentElement.dataset['sidebarCollapsible'] = this.collapsibleMode();
      this.writeCookie(LAYOUT_MODE_COOKIE, this.layoutMode(), 365);
      this.writeCookie(COLLAPSIBLE_MODE_COOKIE, this.collapsibleMode(), 365);
    });

    if (typeof document !== 'undefined') {
      document.addEventListener('keydown', (e) => {
        if ((e.metaKey || e.ctrlKey) && e.key === 'b') {
          e.preventDefault();
          this.toggleSidebar();
        }
      });
    }
  }

  toggleSidebar(): void {
    if (this.collapsibleMode() !== 'none') {
      this.sidebarOpen.update(v => !v);
    }
  }

  setLayoutMode(mode: SidebarLayoutMode): void {
    this.layoutMode.set(mode);
  }

  setCollapsibleMode(mode: SidebarCollapsibleMode): void {
    this.collapsibleMode.set(mode);
    if (mode === 'none') {
      this.sidebarOpen.set(true);
    }
  }

  private readCookie(name: string): string | null {
    if (typeof document === 'undefined') return null;
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? decodeURIComponent(match[2]) : null;
  }

  private writeCookie(name: string, value: string, days: number): void {
    if (typeof document === 'undefined') return;
    const expires = new Date(Date.now() + days * 864e5).toUTCString();
    document.cookie = `${name}=${value}; path=/; expires=${expires}`;
  }
}
