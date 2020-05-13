import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlataforma } from 'app/shared/model/plataforma.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PlataformaService } from './plataforma.service';
import { PlataformaDeleteDialogComponent } from './plataforma-delete-dialog.component';

@Component({
  selector: 'jhi-plataforma',
  templateUrl: './plataforma.component.html'
})
export class PlataformaComponent implements OnInit, OnDestroy {
  plataformas: IPlataforma[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected plataformaService: PlataformaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.plataformas = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.plataformaService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IPlataforma[]>) => this.paginatePlataformas(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.plataformas = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPlataformas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlataforma): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPlataformas(): void {
    this.eventSubscriber = this.eventManager.subscribe('plataformaListModification', () => this.reset());
  }

  delete(plataforma: IPlataforma): void {
    const modalRef = this.modalService.open(PlataformaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plataforma = plataforma;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePlataformas(data: IPlataforma[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.plataformas.push(data[i]);
      }
    }
  }
}
