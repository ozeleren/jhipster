import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Instructor } from './instructor.model';
import { InstructorPopupService } from './instructor-popup.service';
import { InstructorService } from './instructor.service';
import { Department, DepartmentService } from '../department';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-instructor-dialog',
    templateUrl: './instructor-dialog.component.html'
})
export class InstructorDialogComponent implements OnInit {

    instructor: Instructor;
    isSaving: boolean;

    departments: Department[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private instructorService: InstructorService,
        private departmentService: DepartmentService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.departmentService.query()
            .subscribe((res: ResponseWrapper) => { this.departments = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.instructor.id !== undefined) {
            this.subscribeToSaveResponse(
                this.instructorService.update(this.instructor));
        } else {
            this.subscribeToSaveResponse(
                this.instructorService.create(this.instructor));
        }
    }

    private subscribeToSaveResponse(result: Observable<Instructor>) {
        result.subscribe((res: Instructor) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Instructor) {
        this.eventManager.broadcast({ name: 'instructorListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDepartmentById(index: number, item: Department) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-instructor-popup',
    template: ''
})
export class InstructorPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private instructorPopupService: InstructorPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.instructorPopupService
                    .open(InstructorDialogComponent as Component, params['id']);
            } else {
                this.instructorPopupService
                    .open(InstructorDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
