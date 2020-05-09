export interface ITime {
  id?: number;
  nome?: string;
  logo?: string;
}

export class Time implements ITime {
  constructor(public id?: number, public nome?: string, public logo?: string) {}
}
