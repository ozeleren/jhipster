import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Take } from './take.model';
import { TakePopupService } from './take-popup.service';
import { TakeService } from './take.service';

@Component({
    selector: 'jhi-take-delete-dialog',
    templateUrl: './take-delete-dialog.component.html'
})
export class TakeDeleteDialogComponent {

    take: Take;

    constructor(
        private takeService: TakeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.takeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'takeListModification',
                content: 'Deleted an take'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-take-delete-popup',
    template: ''
})
export class TakeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private takePopupService: TakePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.takePopupService
                .open(TakeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
