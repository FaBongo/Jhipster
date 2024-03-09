import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategorie } from '../categorie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../categorie.test-samples';

import { CategorieService } from './categorie.service';

const requireRestSample: ICategorie = {
  ...sampleWithRequiredData,
};

describe('Categorie Service', () => {
  let service: CategorieService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategorie | ICategorie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategorieService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Categorie', () => {
      const categorie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(categorie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Categorie', () => {
      const categorie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(categorie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Categorie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Categorie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Categorie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCategorieToCollectionIfMissing', () => {
      it('should add a Categorie to an empty array', () => {
        const categorie: ICategorie = sampleWithRequiredData;
        expectedResult = service.addCategorieToCollectionIfMissing([], categorie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categorie);
      });

      it('should not add a Categorie to an array that contains it', () => {
        const categorie: ICategorie = sampleWithRequiredData;
        const categorieCollection: ICategorie[] = [
          {
            ...categorie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, categorie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Categorie to an array that doesn't contain it", () => {
        const categorie: ICategorie = sampleWithRequiredData;
        const categorieCollection: ICategorie[] = [sampleWithPartialData];
        expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, categorie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categorie);
      });

      it('should add only unique Categorie to an array', () => {
        const categorieArray: ICategorie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categorieCollection: ICategorie[] = [sampleWithRequiredData];
        expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, ...categorieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categorie: ICategorie = sampleWithRequiredData;
        const categorie2: ICategorie = sampleWithPartialData;
        expectedResult = service.addCategorieToCollectionIfMissing([], categorie, categorie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categorie);
        expect(expectedResult).toContain(categorie2);
      });

      it('should accept null and undefined values', () => {
        const categorie: ICategorie = sampleWithRequiredData;
        expectedResult = service.addCategorieToCollectionIfMissing([], null, categorie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categorie);
      });

      it('should return initial array if no Categorie is added', () => {
        const categorieCollection: ICategorie[] = [sampleWithRequiredData];
        expectedResult = service.addCategorieToCollectionIfMissing(categorieCollection, undefined, null);
        expect(expectedResult).toEqual(categorieCollection);
      });
    });

    describe('compareCategorie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategorie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCategorie(entity1, entity2);
        const compareResult2 = service.compareCategorie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCategorie(entity1, entity2);
        const compareResult2 = service.compareCategorie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCategorie(entity1, entity2);
        const compareResult2 = service.compareCategorie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
