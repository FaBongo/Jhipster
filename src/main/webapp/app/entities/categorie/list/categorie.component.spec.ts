import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CategorieService } from '../service/categorie.service';

import { CategorieComponent } from './categorie.component';

describe('Categorie Management Component', () => {
  let comp: CategorieComponent;
  let fixture: ComponentFixture<CategorieComponent>;
  let service: CategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'categorie', component: CategorieComponent }]),
        HttpClientTestingModule,
        CategorieComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(CategorieComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategorieComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CategorieService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.categories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to categorieService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCategorieIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCategorieIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
