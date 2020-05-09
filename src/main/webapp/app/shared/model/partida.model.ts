import { Moment } from 'moment';
import { ITime } from 'app/shared/model/time.model';
import { ICampeonato } from 'app/shared/model/campeonato.model';

export interface IPartida {
  id?: number;
  golsVisitante?: number;
  golsMandante?: number;
  local?: string;
  data?: Moment;
  mandante?: ITime;
  visitante?: ITime;
  campeonato?: ICampeonato;
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
    public campeonato?: ICampeonato
  ) {}
}
