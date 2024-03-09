import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategorie, NewCategorie } from '../categorie.model';

export type PartialUpdateCategorie = Partial<ICategorie> & Pick<ICategorie, 'id'>;

export type EntityResponseType = HttpResponse<ICategorie>;
export type EntityArrayResponseType = HttpResponse<ICategorie[]>;

@Injectable({ providedIn: 'root' })
export class CategorieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categories');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(categorie: NewCategorie): Observable<EntityResponseType> {
    return this.http.post<ICategorie>(this.resourceUrl, categorie, { observe: 'response' });
  }

  update(categorie: ICategorie): Observable<EntityResponseType> {
    return this.http.put<ICategorie>(`${this.resourceUrl}/${this.getCategorieIdentifier(categorie)}`, categorie, { observe: 'response' });
  }

  partialUpdate(categorie: PartialUpdateCategorie): Observable<EntityResponseType> {
    return this.http.patch<ICategorie>(`${this.resourceUrl}/${this.getCategorieIdentifier(categorie)}`, categorie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategorie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategorie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCategorieIdentifier(categorie: Pick<ICategorie, 'id'>): number {
    return categorie.id;
  }

  compareCategorie(o1: Pick<ICategorie, 'id'> | null, o2: Pick<ICategorie, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategorieIdentifier(o1) === this.getCategorieIdentifier(o2) : o1 === o2;
  }

  addCategorieToCollectionIfMissing<Type extends Pick<ICategorie, 'id'>>(
    categorieCollection: Type[],
    ...categoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categories: Type[] = categoriesToCheck.filter(isPresent);
    if (categories.length > 0) {
      const categorieCollectionIdentifiers = categorieCollection.map(categorieItem => this.getCategorieIdentifier(categorieItem)!);
      const categoriesToAdd = categories.filter(categorieItem => {
        const categorieIdentifier = this.getCategorieIdentifier(categorieItem);
        if (categorieCollectionIdentifiers.includes(categorieIdentifier)) {
          return false;
        }
        categorieCollectionIdentifiers.push(categorieIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...categorieCollection];
    }
    return categorieCollection;
  }
}
