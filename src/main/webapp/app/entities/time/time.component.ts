import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITime } from 'app/shared/model/time.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TimeService } from './time.service';
import { TimeDeleteDialogComponent } from './time-delete-dialog.component';

@Component({
  selector: 'jhi-time',
  templateUrl: './time.component.html'
})
export class TimeComponent implements OnInit, OnDestroy {
  times: ITime[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected timeService: TimeService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.times = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.timeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<ITime[]>) => this.paginateTimes(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.times = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTimes();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITime): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTimes(): void {
    this.eventSubscriber = this.eventManager.subscribe('timeListModification', () => this.reset());
  }

  delete(time: ITime): void {
    const modalRef = this.modalService.open(TimeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.time = time;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateTimes(data: ITime[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.times.push(data[i]);
      }
    }
  }
}
