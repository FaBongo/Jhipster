import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';
import { CategorieFormService, CategorieFormGroup } from './categorie-form.service';

@Component({
  standalone: true,
  selector: 'jhi-categorie-update',
  templateUrl: './categorie-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CategorieUpdateComponent implements OnInit {
  isSaving = false;
  categorie: ICategorie | null = null;

  categoriesSharedCollection: ICategorie[] = [];

  editForm: CategorieFormGroup = this.categorieFormService.createCategorieFormGroup();

  constructor(
    protected categorieService: CategorieService,
    protected categorieFormService: CategorieFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareCategorie = (o1: ICategorie | null, o2: ICategorie | null): boolean => this.categorieService.compareCategorie(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categorie }) => {
      this.categorie = categorie;
      if (categorie) {
        this.updateForm(categorie);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categorie = this.categorieFormService.getCategorie(this.editForm);
    if (categorie.id !== null) {
      this.subscribeToSaveResponse(this.categorieService.update(categorie));
    } else {
      this.subscribeToSaveResponse(this.categorieService.create(categorie));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategorie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(categorie: ICategorie): void {
    this.categorie = categorie;
    this.categorieFormService.resetForm(this.editForm, categorie);

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(
      this.categoriesSharedCollection,
      categorie.parent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(categories, this.categorie?.parent),
        ),
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }
}
