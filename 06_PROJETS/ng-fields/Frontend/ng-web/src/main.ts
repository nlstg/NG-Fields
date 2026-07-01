import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

let version = window.localStorage.getItem('theme');
if (!version) {
  version = 'brutalist';
  window.localStorage.setItem('theme', version);
}

const theme = document.createElement('link');
theme.rel = 'stylesheet';
theme.href = `/assets/theme/presets/${version}.css`;
document.head.appendChild(theme);

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
