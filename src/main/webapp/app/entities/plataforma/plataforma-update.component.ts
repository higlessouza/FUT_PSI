import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPlataforma, Plataforma } from 'app/shared/model/plataforma.model';
import { PlataformaService } from './plataforma.service';

@Component({
  selector: 'jhi-plataforma-update',
  templateUrl: './plataforma-update.component.html'
})
export class PlataformaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    logo: [],
    marca: []
  });

  constructor(protected plataformaService: PlataformaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plataforma }) => {
      this.updateForm(plataforma);
    });
  }

  updateForm(plataforma: IPlataforma): void {
    this.editForm.patchValue({
      id: plataforma.id,
      nome: plataforma.nome,
      logo: plataforma.logo,
      marca: plataforma.marca
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plataforma = this.createFromForm();
    if (plataforma.id !== undefined) {
      this.subscribeToSaveResponse(this.plataformaService.update(plataforma));
    } else {
      this.subscribeToSaveResponse(this.plataformaService.create(plataforma));
    }
  }

  private createFromForm(): IPlataforma {
    return {
      ...new Plataforma(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      marca: this.editForm.get(['marca'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlataforma>>): void {
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
