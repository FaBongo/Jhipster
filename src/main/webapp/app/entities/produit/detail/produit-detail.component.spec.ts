import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProduitDetailComponent } from './produit-detail.component';

describe('Produit Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProduitDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProduitDetailComponent,
              resolve: { produit: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProduitDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load produit on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProduitDetailComponent);

      // THEN
      expect(instance.produit).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
