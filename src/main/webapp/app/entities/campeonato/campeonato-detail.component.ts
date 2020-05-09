import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICampeonato } from 'app/shared/model/campeonato.model';

@Component({
  selector: 'jhi-campeonato-detail',
  templateUrl: './campeonato-detail.component.html'
})
export class CampeonatoDetailComponent implements OnInit {
  campeonato: ICampeonato | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campeonato }) => (this.campeonato = campeonato));
  }

  previousState(): void {
    window.history.back();
  }
}
