import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Take } from './take.model';
import { TakePopupService } from './take-popup.service';
import { TakeService } from './take.service';
import { Student, StudentService } from '../student';
import { Course, CourseService } from '../course';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-take-dialog',
    templateUrl: './take-dialog.component.html'
})
export class TakeDialogComponent implements OnInit {

    take: Take;
    isSaving: boolean;

    students: Student[];

    courses: Course[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private takeService: TakeService,
        private studentService: StudentService,
        private courseService: CourseService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.studentService.query()
            .subscribe((res: ResponseWrapper) => { this.students = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.take.id !== undefined) {
            this.subscribeToSaveResponse(
                this.takeService.update(this.take));
        } else {
            this.subscribeToSaveResponse(
                this.takeService.create(this.take));
        }
    }

    private subscribeToSaveResponse(result: Observable<Take>) {
        result.subscribe((res: Take) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Take) {
        this.eventManager.broadcast({ name: 'takeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackStudentById(index: number, item: Student) {
        return item.id;
    }

    trackCourseById(index: number, item: Course) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-take-popup',
    template: ''
})
export class TakePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private takePopupService: TakePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.takePopupService
                    .open(TakeDialogComponent as Component, params['id']);
            } else {
                this.takePopupService
                    .open(TakeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
