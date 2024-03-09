import { ICategorie, NewCategorie } from './categorie.model';

export const sampleWithRequiredData: ICategorie = {
  id: 12322,
  nom: 'dense ha',
};

export const sampleWithPartialData: ICategorie = {
  id: 16319,
  nom: 'lorsque athlète',
  nbreProduit: 13764,
};

export const sampleWithFullData: ICategorie = {
  id: 435,
  nom: 'gratis',
  description: 'de manière à ce que',
  depth: 8763,
  nbreProduit: 10276,
};

export const sampleWithNewData: NewCategorie = {
  nom: 'reporter hôte',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
