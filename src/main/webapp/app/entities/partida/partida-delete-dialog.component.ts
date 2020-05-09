import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPartida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';

@Component({
  templateUrl: './partida-delete-dialog.component.html'
})
export class PartidaDeleteDialogComponent {
  partida?: IPartida;

  constructor(protected partidaService: PartidaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partidaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('partidaListModification');
      this.activeModal.close();
    });
  }
}
