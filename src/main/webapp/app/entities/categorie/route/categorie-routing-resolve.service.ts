import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';

export const categorieResolve = (route: ActivatedRouteSnapshot): Observable<null | ICategorie> => {
  const id = route.params['id'];
  if (id) {
    return inject(CategorieService)
      .find(id)
      .pipe(
        mergeMap((categorie: HttpResponse<ICategorie>) => {
          if (categorie.body) {
            return of(categorie.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default categorieResolve;
