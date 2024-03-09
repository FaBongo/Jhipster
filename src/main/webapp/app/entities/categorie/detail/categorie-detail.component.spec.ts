import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CategorieDetailComponent } from './categorie-detail.component';

describe('Categorie Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategorieDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CategorieDetailComponent,
              resolve: { categorie: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CategorieDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load categorie on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CategorieDetailComponent);

      // THEN
      expect(instance.categorie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
