import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlataforma } from 'app/shared/model/plataforma.model';

@Component({
  selector: 'jhi-plataforma-detail',
  templateUrl: './plataforma-detail.component.html'
})
export class PlataformaDetailComponent implements OnInit {
  plataforma: IPlataforma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plataforma }) => (this.plataforma = plataforma));
  }

  previousState(): void {
    window.history.back();
  }
}
