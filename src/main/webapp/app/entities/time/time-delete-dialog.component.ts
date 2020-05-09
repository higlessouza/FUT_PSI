import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITime } from 'app/shared/model/time.model';
import { TimeService } from './time.service';

@Component({
  templateUrl: './time-delete-dialog.component.html'
})
export class TimeDeleteDialogComponent {
  time?: ITime;

  constructor(protected timeService: TimeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('timeListModification');
      this.activeModal.close();
    });
  }
}
