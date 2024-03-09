import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface IProduit {
  id: number;
  nom?: string | null;
  prix?: number | null;
  description?: string | null;
  categorie?: ICategorie | null;
}

export type NewProduit = Omit<IProduit, 'id'> & { id: null };
