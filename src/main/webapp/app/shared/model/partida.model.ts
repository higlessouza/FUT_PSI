import { Moment } from 'moment';
import { ITime } from 'app/shared/model/time.model';
import { ICampeonato } from 'app/shared/model/campeonato.model';
import { IPlataforma } from 'app/shared/model/plataforma.model';

export interface IPartida {
  id?: number;
  golsVisitante?: number;
  golsMandante?: number;
  local?: string;
  data?: Moment;
  mandante?: ITime;
  visitante?: ITime;
  campeonato?: ICampeonato;
  plataforma?: IPlataforma;
}

export class Partida implements IPartida {
  constructor(
    public id?: number,
    public golsVisitante?: number,
    public golsMandante?: number,
    public local?: string,
    public data?: Moment,
    public mandante?: ITime,
    public visitante?: ITime,
    public campeonato?: ICampeonato,
    public plataforma?: IPlataforma
  ) {}
}
