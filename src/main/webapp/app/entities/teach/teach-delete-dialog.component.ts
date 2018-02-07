import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Teach } from './teach.model';
import { TeachPopupService } from './teach-popup.service';
import { TeachService } from './teach.service';

@Component({
    selector: 'jhi-teach-delete-dialog',
    templateUrl: './teach-delete-dialog.component.html'
})
export class TeachDeleteDialogComponent {

    teach: Teach;

    constructor(
        private teachService: TeachService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.teachService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'teachListModification',
                content: 'Deleted an teach'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-teach-delete-popup',
    template: ''
})
export class TeachDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teachPopupService: TeachPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.teachPopupService
                .open(TeachDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
