import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartida } from 'app/shared/model/partida.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PartidaService } from './partida.service';
import { PartidaDeleteDialogComponent } from './partida-delete-dialog.component';

@Component({
  selector: 'jhi-partida',
  templateUrl: './partida.component.html'
})
export class PartidaComponent implements OnInit, OnDestroy {
  partidas: IPartida[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected partidaService: PartidaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.partidas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.partidaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPartida[]>) => this.paginatePartidas(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.partidas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPartidas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPartida): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPartidas(): void {
    this.eventSubscriber = this.eventManager.subscribe('partidaListModification', () => this.reset());
  }

  delete(partida: IPartida): void {
    const modalRef = this.modalService.open(PartidaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partida = partida;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePartidas(data: IPartida[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.partidas.push(data[i]);
      }
    }
  }
}
