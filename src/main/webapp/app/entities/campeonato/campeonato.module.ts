import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FutPsiSharedModule } from 'app/shared/shared.module';
import { CampeonatoComponent } from './campeonato.component';
import { CampeonatoDetailComponent } from './campeonato-detail.component';
import { CampeonatoUpdateComponent } from './campeonato-update.component';
import { CampeonatoDeleteDialogComponent } from './campeonato-delete-dialog.component';
import { campeonatoRoute } from './campeonato.route';

@NgModule({
  imports: [FutPsiSharedModule, RouterModule.forChild(campeonatoRoute)],
  declarations: [CampeonatoComponent, CampeonatoDetailComponent, CampeonatoUpdateComponent, CampeonatoDeleteDialogComponent],
  entryComponents: [CampeonatoDeleteDialogComponent]
})
export class FutPsiCampeonatoModule {}
