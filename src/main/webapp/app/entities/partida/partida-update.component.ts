import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPartida, Partida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';
import { ITime } from 'app/shared/model/time.model';
import { TimeService } from 'app/entities/time/time.service';
import { ICampeonato } from 'app/shared/model/campeonato.model';
import { CampeonatoService } from 'app/entities/campeonato/campeonato.service';
import { IPlataforma } from 'app/shared/model/plataforma.model';
import { PlataformaService } from 'app/entities/plataforma/plataforma.service';

type SelectableEntity = ITime | ICampeonato | IPlataforma;

@Component({
  selector: 'jhi-partida-update',
  templateUrl: './partida-update.component.html'
})
export class PartidaUpdateComponent implements OnInit {
  isSaving = false;
  mandantes: ITime[] = [];
  visitantes: ITime[] = [];
  campeonatoes: ICampeonato[] = [];
  plataformas: IPlataforma[] = [];
  dataDp: any;

  editForm = this.fb.group({
    id: [],
    golsVisitante: [],
    golsMandante: [],
    local: [],
    data: [],
    mandante: [],
    visitante: [],
    campeonato: [],
    plataforma: []
  });

  constructor(
    protected partidaService: PartidaService,
    protected timeService: TimeService,
    protected campeonatoService: CampeonatoService,
    protected plataformaService: PlataformaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partida }) => {
      this.updateForm(partida);

      this.timeService
        .query({ filter: 'partida-is-null' })
        .pipe(
          map((res: HttpResponse<ITime[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITime[]) => {
          if (!partida.mandante || !partida.mandante.id) {
            this.mandantes = resBody;
          } else {
            this.timeService
              .find(partida.mandante.id)
              .pipe(
                map((subRes: HttpResponse<ITime>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITime[]) => (this.mandantes = concatRes));
          }
        });

      this.timeService
        .query({ filter: 'partida-is-null' })
        .pipe(
          map((res: HttpResponse<ITime[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITime[]) => {
          if (!partida.visitante || !partida.visitante.id) {
            this.visitantes = resBody;
          } else {
            this.timeService
              .find(partida.visitante.id)
              .pipe(
                map((subRes: HttpResponse<ITime>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITime[]) => (this.visitantes = concatRes));
          }
        });

      this.campeonatoService
        .query({ filter: 'partida-is-null' })
        .pipe(
          map((res: HttpResponse<ICampeonato[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICampeonato[]) => {
          if (!partida.campeonato || !partida.campeonato.id) {
            this.campeonatoes = resBody;
          } else {
            this.campeonatoService
              .find(partida.campeonato.id)
              .pipe(
                map((subRes: HttpResponse<ICampeonato>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICampeonato[]) => (this.campeonatoes = concatRes));
          }
        });

      this.plataformaService
        .query({ filter: 'partida-is-null' })
        .pipe(
          map((res: HttpResponse<IPlataforma[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPlataforma[]) => {
          if (!partida.plataforma || !partida.plataforma.id) {
            this.plataformas = resBody;
          } else {
            this.plataformaService
              .find(partida.plataforma.id)
              .pipe(
                map((subRes: HttpResponse<IPlataforma>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPlataforma[]) => (this.plataformas = concatRes));
          }
        });
    });
  }

  updateForm(partida: IPartida): void {
    this.editForm.patchValue({
      id: partida.id,
      golsVisitante: partida.golsVisitante,
      golsMandante: partida.golsMandante,
      local: partida.local,
      data: partida.data,
      mandante: partida.mandante,
      visitante: partida.visitante,
      campeonato: partida.campeonato,
      plataforma: partida.plataforma
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partida = this.createFromForm();
    if (partida.id !== undefined) {
      this.subscribeToSaveResponse(this.partidaService.update(partida));
    } else {
      this.subscribeToSaveResponse(this.partidaService.create(partida));
    }
  }

  private createFromForm(): IPartida {
    return {
      ...new Partida(),
      id: this.editForm.get(['id'])!.value,
      golsVisitante: this.editForm.get(['golsVisitante'])!.value,
      golsMandante: this.editForm.get(['golsMandante'])!.value,
      local: this.editForm.get(['local'])!.value,
      data: this.editForm.get(['data'])!.value,
      mandante: this.editForm.get(['mandante'])!.value,
      visitante: this.editForm.get(['visitante'])!.value,
      campeonato: this.editForm.get(['campeonato'])!.value,
      plataforma: this.editForm.get(['plataforma'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartida>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
