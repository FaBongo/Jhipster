import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'categorie',
    data: { pageTitle: 'jhipsterMicroservicesApp.categorie.home.title' },
    loadChildren: () => import('./categorie/categorie.routes'),
  },
  {
    path: 'produit',
    data: { pageTitle: 'jhipsterMicroservicesApp.produit.home.title' },
    loadChildren: () => import('./produit/produit.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
