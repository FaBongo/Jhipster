import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProduit } from '../produit.model';
import { ProduitService } from '../service/produit.service';

@Component({
  standalone: true,
  templateUrl: './produit-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProduitDeleteDialogComponent {
  produit?: IProduit;

  constructor(
    protected produitService: ProduitService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.produitService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
