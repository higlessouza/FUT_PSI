import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITime, Time } from 'app/shared/model/time.model';
import { TimeService } from './time.service';

@Component({
  selector: 'jhi-time-update',
  templateUrl: './time-update.component.html'
})
export class TimeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nome: [],
    logo: []
  });

  constructor(protected timeService: TimeService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ time }) => {
      this.updateForm(time);
    });
  }

  updateForm(time: ITime): void {
    this.editForm.patchValue({
      id: time.id,
      nome: time.nome,
      logo: time.logo
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const time = this.createFromForm();
    if (time.id !== undefined) {
      this.subscribeToSaveResponse(this.timeService.update(time));
    } else {
      this.subscribeToSaveResponse(this.timeService.create(time));
    }
  }

  private createFromForm(): ITime {
    return {
      ...new Time(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      logo: this.editForm.get(['logo'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITime>>): void {
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
