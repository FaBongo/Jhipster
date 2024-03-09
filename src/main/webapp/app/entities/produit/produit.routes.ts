import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProduitComponent } from './list/produit.component';
import { ProduitDetailComponent } from './detail/produit-detail.component';
import { ProduitUpdateComponent } from './update/produit-update.component';
import ProduitResolve from './route/produit-routing-resolve.service';

const produitRoute: Routes = [
  {
    path: '',
    component: ProduitComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProduitDetailComponent,
    resolve: {
      produit: ProduitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProduitUpdateComponent,
    resolve: {
      produit: ProduitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProduitUpdateComponent,
    resolve: {
      produit: ProduitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default produitRoute;
