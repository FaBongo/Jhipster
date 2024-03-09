import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategorieService } from '../service/categorie.service';
import { ICategorie } from '../categorie.model';
import { CategorieFormService } from './categorie-form.service';

import { CategorieUpdateComponent } from './categorie-update.component';

describe('Categorie Management Update Component', () => {
  let comp: CategorieUpdateComponent;
  let fixture: ComponentFixture<CategorieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categorieFormService: CategorieFormService;
  let categorieService: CategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CategorieUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CategorieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategorieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categorieFormService = TestBed.inject(CategorieFormService);
    categorieService = TestBed.inject(CategorieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categorie query and add missing value', () => {
      const categorie: ICategorie = { id: 456 };
      const parent: ICategorie = { id: 10071 };
      categorie.parent = parent;

      const categorieCollection: ICategorie[] = [{ id: 17418 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [parent];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ categorie });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(
        categorieCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const categorie: ICategorie = { id: 456 };
      const parent: ICategorie = { id: 90 };
      categorie.parent = parent;

      activatedRoute.data = of({ categorie });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(parent);
      expect(comp.categorie).toEqual(categorie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategorie>>();
      const categorie = { id: 123 };
      jest.spyOn(categorieFormService, 'getCategorie').mockReturnValue(categorie);
      jest.spyOn(categorieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categorie }));
      saveSubject.complete();

      // THEN
      expect(categorieFormService.getCategorie).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categorieService.update).toHaveBeenCalledWith(expect.objectContaining(categorie));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategorie>>();
      const categorie = { id: 123 };
      jest.spyOn(categorieFormService, 'getCategorie').mockReturnValue({ id: null });
      jest.spyOn(categorieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorie: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categorie }));
      saveSubject.complete();

      // THEN
      expect(categorieFormService.getCategorie).toHaveBeenCalled();
      expect(categorieService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategorie>>();
      const categorie = { id: 123 };
      jest.spyOn(categorieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categorieService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategorie', () => {
      it('Should forward to categorieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categorieService, 'compareCategorie');
        comp.compareCategorie(entity, entity2);
        expect(categorieService.compareCategorie).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
