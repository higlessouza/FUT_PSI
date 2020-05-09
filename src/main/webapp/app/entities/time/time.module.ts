import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FutPsiSharedModule } from 'app/shared/shared.module';
import { TimeComponent } from './time.component';
import { TimeDetailComponent } from './time-detail.component';
import { TimeUpdateComponent } from './time-update.component';
import { TimeDeleteDialogComponent } from './time-delete-dialog.component';
import { timeRoute } from './time.route';

@NgModule({
  imports: [FutPsiSharedModule, RouterModule.forChild(timeRoute)],
  declarations: [TimeComponent, TimeDetailComponent, TimeUpdateComponent, TimeDeleteDialogComponent],
  entryComponents: [TimeDeleteDialogComponent]
})
export class FutPsiTimeModule {}
