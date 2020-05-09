export interface ICampeonato {
  id?: number;
  nome?: string;
  logo?: string;
  cidade?: string;
}

export class Campeonato implements ICampeonato {
  constructor(public id?: number, public nome?: string, public logo?: string, public cidade?: string) {}
}
