export interface ICategorie {
  id: number;
  nom?: string | null;
  description?: string | null;
  depth?: number | null;
  nbreProduit?: number | null;
  parent?: ICategorie | null;
}

export type NewCategorie = Omit<ICategorie, 'id'> & { id: null };
