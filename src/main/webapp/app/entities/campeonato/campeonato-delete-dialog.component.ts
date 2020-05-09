import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICampeonato } from 'app/shared/model/campeonato.model';
import { CampeonatoService } from './campeonato.service';

@Component({
  templateUrl: './campeonato-delete-dialog.component.html'
})
export class CampeonatoDeleteDialogComponent {
  campeonato?: ICampeonato;

  constructor(
    protected campeonatoService: CampeonatoService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.campeonatoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('campeonatoListModification');
      this.activeModal.close();
    });
  }
}
