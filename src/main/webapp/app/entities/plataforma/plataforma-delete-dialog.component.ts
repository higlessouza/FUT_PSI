import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlataforma } from 'app/shared/model/plataforma.model';
import { PlataformaService } from './plataforma.service';

@Component({
  templateUrl: './plataforma-delete-dialog.component.html'
})
export class PlataformaDeleteDialogComponent {
  plataforma?: IPlataforma;

  constructor(
    protected plataformaService: PlataformaService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plataformaService.delete(id).subscribe(() => {
      this.eventManager.broadcast('plataformaListModification');
      this.activeModal.close();
    });
  }
}
