import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors, withInterceptorsFromDi} from "@angular/common/http";
import {authInterceptor} from "./interceptors/auth.interceptor";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AdressFormatPipe} from "./pipe/adress-format.pipe";
import {MessageService} from "primeng/api";
import {DialogModule} from "primeng/dialog";
import {DialogService, DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";

export const appConfig: ApplicationConfig = {
  providers: [MessageService, DialogService, DynamicDialogRef, DynamicDialogConfig,  DialogModule, importProvidersFrom([BrowserAnimationsModule]),provideRouter(routes), provideHttpClient(withInterceptors([authInterceptor])), AdressFormatPipe]
};
