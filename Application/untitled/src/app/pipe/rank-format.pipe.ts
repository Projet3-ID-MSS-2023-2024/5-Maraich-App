import { Pipe, PipeTransform } from '@angular/core';
import {RankEnum} from "../models/rankEnum";

@Pipe({
  name: 'rankFormat',
  standalone: true
})
export class RankFormatPipe implements PipeTransform {

  transform(rank: RankEnum): string {
    if (!rank) {
      return '';
    }

    // Mappez les valeurs de l'énumération aux valeurs en français
    const rankTranslations: Record<RankEnum, string> = {
      [RankEnum.CUSTOMER]: 'Client',
      [RankEnum.MARAICHER]: 'Maraîcher',
      [RankEnum.ADMINISTRATOR]: 'Administrateur'
    };

    return rankTranslations[rank] || '';
  }

}
