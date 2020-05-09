import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITime } from 'app/shared/model/time.model';

type EntityResponseType = HttpResponse<ITime>;
type EntityArrayResponseType = HttpResponse<ITime[]>;

@Injectable({ providedIn: 'root' })
export class TimeService {
  public resourceUrl = SERVER_API_URL + 'api/times';

  constructor(protected http: HttpClient) {}

  create(time: ITime): Observable<EntityResponseType> {
    return this.http.post<ITime>(this.resourceUrl, time, { observe: 'response' });
  }

  update(time: ITime): Observable<EntityResponseType> {
    return this.http.put<ITime>(this.resourceUrl, time, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITime>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITime[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
