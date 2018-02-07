import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Instructor } from './instructor.model';
import { InstructorPopupService } from './instructor-popup.service';
import { InstructorService } from './instructor.service';

@Component({
    selector: 'jhi-instructor-delete-dialog',
    templateUrl: './instructor-delete-dialog.component.html'
})
export class InstructorDeleteDialogComponent {

    instructor: Instructor;

    constructor(
        private instructorService: InstructorService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.instructorService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'instructorListModification',
                content: 'Deleted an instructor'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-instructor-delete-popup',
    template: ''
})
export class InstructorDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private instructorPopupService: InstructorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.instructorPopupService
                .open(InstructorDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
