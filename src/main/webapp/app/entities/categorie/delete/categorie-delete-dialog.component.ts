import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICategorie } from '../categorie.model';
import { CategorieService } from '../service/categorie.service';

@Component({
  standalone: true,
  templateUrl: './categorie-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CategorieDeleteDialogComponent {
  categorie?: ICategorie;

  constructor(
    protected categorieService: CategorieService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categorieService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
