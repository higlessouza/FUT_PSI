import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICampeonato } from 'app/shared/model/campeonato.model';

type EntityResponseType = HttpResponse<ICampeonato>;
type EntityArrayResponseType = HttpResponse<ICampeonato[]>;

@Injectable({ providedIn: 'root' })
export class CampeonatoService {
  public resourceUrl = SERVER_API_URL + 'api/campeonatoes';

  constructor(protected http: HttpClient) {}

  create(campeonato: ICampeonato): Observable<EntityResponseType> {
    return this.http.post<ICampeonato>(this.resourceUrl, campeonato, { observe: 'response' });
  }

  update(campeonato: ICampeonato): Observable<EntityResponseType> {
    return this.http.put<ICampeonato>(this.resourceUrl, campeonato, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICampeonato>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICampeonato[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
