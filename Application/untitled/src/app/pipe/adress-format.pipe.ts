import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'adressFormat',
  standalone: true
})
export class AdressFormatPipe implements PipeTransform {

  transform(address: any): string {
    if (!address) {
      return '';
    }

    return `${address.number} ${address.road}, ${address.postCode} ${address.city}`;
  }
}
