import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors, withInterceptorsFromDi} from "@angular/common/http";
import {authInterceptor} from "./interceptors/auth.interceptor";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

export const appConfig: ApplicationConfig = {
  providers: [importProvidersFrom([BrowserAnimationsModule]),provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor]))]
};
