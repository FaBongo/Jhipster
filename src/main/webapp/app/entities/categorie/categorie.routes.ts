import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CategorieComponent } from './list/categorie.component';
import { CategorieDetailComponent } from './detail/categorie-detail.component';
import { CategorieUpdateComponent } from './update/categorie-update.component';
import CategorieResolve from './route/categorie-routing-resolve.service';

const categorieRoute: Routes = [
  {
    path: '',
    component: CategorieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategorieDetailComponent,
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategorieUpdateComponent,
    resolve: {
      categorie: CategorieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default categorieRoute;
