import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITime } from 'app/shared/model/time.model';

@Component({
  selector: 'jhi-time-detail',
  templateUrl: './time-detail.component.html'
})
export class TimeDetailComponent implements OnInit {
  time: ITime | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ time }) => (this.time = time));
  }

  previousState(): void {
    window.history.back();
  }
}
