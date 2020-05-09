import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPartida } from 'app/shared/model/partida.model';

type EntityResponseType = HttpResponse<IPartida>;
type EntityArrayResponseType = HttpResponse<IPartida[]>;

@Injectable({ providedIn: 'root' })
export class PartidaService {
  public resourceUrl = SERVER_API_URL + 'api/partidas';

  constructor(protected http: HttpClient) {}

  create(partida: IPartida): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partida);
    return this.http
      .post<IPartida>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(partida: IPartida): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(partida);
    return this.http
      .put<IPartida>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPartida>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPartida[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(partida: IPartida): IPartida {
    const copy: IPartida = Object.assign({}, partida, {
      data: partida.data && partida.data.isValid() ? partida.data.format(DATE_FORMAT) : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.data = res.body.data ? moment(res.body.data) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((partida: IPartida) => {
        partida.data = partida.data ? moment(partida.data) : undefined;
      });
    }
    return res;
  }
}
