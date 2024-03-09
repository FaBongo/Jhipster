import { IProduit, NewProduit } from './produit.model';

export const sampleWithRequiredData: IProduit = {
  id: 8742,
  nom: 'bang doter environ',
  prix: 26711.77,
};

export const sampleWithPartialData: IProduit = {
  id: 2877,
  nom: 'sage jusque communauté étudiante',
  prix: 25668.08,
};

export const sampleWithFullData: IProduit = {
  id: 12340,
  nom: 'soudain',
  prix: 9612.82,
  description: 'candide',
};

export const sampleWithNewData: NewProduit = {
  nom: "crac à l'insu de",
  prix: 13119.72,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
