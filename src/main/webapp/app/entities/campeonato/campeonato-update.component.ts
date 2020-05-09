import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICampeonato, Campeonato } from 'app/shared/model/campeonato.model';
import { CampeonatoService } from './campeonato.service';

@Component({
  selector: 'jhi-campeonato-update',
  templateUrl: './campeonato-update.component.html'
})
export class CampeonatoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    logo: [],
    cidade: []
  });

  constructor(protected campeonatoService: CampeonatoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campeonato }) => {
      this.updateForm(campeonato);
    });
  }

  updateForm(campeonato: ICampeonato): void {
    this.editForm.patchValue({
      id: campeonato.id,
      nome: campeonato.nome,
      logo: campeonato.logo,
      cidade: campeonato.cidade
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const campeonato = this.createFromForm();
    if (campeonato.id !== undefined) {
      this.subscribeToSaveResponse(this.campeonatoService.update(campeonato));
    } else {
      this.subscribeToSaveResponse(this.campeonatoService.create(campeonato));
    }
  }

  private createFromForm(): ICampeonato {
    return {
      ...new Campeonato(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      cidade: this.editForm.get(['cidade'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICampeonato>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
