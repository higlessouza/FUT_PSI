import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICampeonato } from 'app/shared/model/campeonato.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CampeonatoService } from './campeonato.service';
import { CampeonatoDeleteDialogComponent } from './campeonato-delete-dialog.component';

@Component({
  selector: 'jhi-campeonato',
  templateUrl: './campeonato.component.html'
})
export class CampeonatoComponent implements OnInit, OnDestroy {
  campeonatoes: ICampeonato[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected campeonatoService: CampeonatoService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.campeonatoes = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.campeonatoService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ICampeonato[]>) => this.paginateCampeonatoes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.campeonatoes = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCampeonatoes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICampeonato): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCampeonatoes(): void {
    this.eventSubscriber = this.eventManager.subscribe('campeonatoListModification', () => this.reset());
  }

  delete(campeonato: ICampeonato): void {
    const modalRef = this.modalService.open(CampeonatoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.campeonato = campeonato;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCampeonatoes(data: ICampeonato[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.campeonatoes.push(data[i]);
      }
    }
  }
}
