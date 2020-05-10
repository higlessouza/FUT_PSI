export interface IPlataforma {
  id?: number;
  nome?: string;
  logo?: string;
  marca?: string;
}

export class Plataforma implements IPlataforma {
  constructor(public id?: number, public nome?: string, public logo?: string, public marca?: string) {}
}
