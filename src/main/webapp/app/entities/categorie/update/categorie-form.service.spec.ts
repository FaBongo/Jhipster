import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../categorie.test-samples';

import { CategorieFormService } from './categorie-form.service';

describe('Categorie Form Service', () => {
  let service: CategorieFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategorieFormService);
  });

  describe('Service methods', () => {
    describe('createCategorieFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategorieFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            depth: expect.any(Object),
            nbreProduit: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });

      it('passing ICategorie should create a new form with FormGroup', () => {
        const formGroup = service.createCategorieFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            depth: expect.any(Object),
            nbreProduit: expect.any(Object),
            parent: expect.any(Object),
          }),
        );
      });
    });

    describe('getCategorie', () => {
      it('should return NewCategorie for default Categorie initial value', () => {
        const formGroup = service.createCategorieFormGroup(sampleWithNewData);

        const categorie = service.getCategorie(formGroup) as any;

        expect(categorie).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategorie for empty Categorie initial value', () => {
        const formGroup = service.createCategorieFormGroup();

        const categorie = service.getCategorie(formGroup) as any;

        expect(categorie).toMatchObject({});
      });

      it('should return ICategorie', () => {
        const formGroup = service.createCategorieFormGroup(sampleWithRequiredData);

        const categorie = service.getCategorie(formGroup) as any;

        expect(categorie).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategorie should not enable id FormControl', () => {
        const formGroup = service.createCategorieFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategorie should disable id FormControl', () => {
        const formGroup = service.createCategorieFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
