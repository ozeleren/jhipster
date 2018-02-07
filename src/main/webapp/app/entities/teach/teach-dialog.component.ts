import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Teach } from './teach.model';
import { TeachPopupService } from './teach-popup.service';
import { TeachService } from './teach.service';
import { Course, CourseService } from '../course';
import { Instructor, InstructorService } from '../instructor';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-teach-dialog',
    templateUrl: './teach-dialog.component.html'
})
export class TeachDialogComponent implements OnInit {

    teach: Teach;
    isSaving: boolean;

    courses: Course[];

    instructors: Instructor[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private teachService: TeachService,
        private courseService: CourseService,
        private instructorService: InstructorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.instructorService.query()
            .subscribe((res: ResponseWrapper) => { this.instructors = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.teach.id !== undefined) {
            this.subscribeToSaveResponse(
                this.teachService.update(this.teach));
        } else {
            this.subscribeToSaveResponse(
                this.teachService.create(this.teach));
        }
    }

    private subscribeToSaveResponse(result: Observable<Teach>) {
        result.subscribe((res: Teach) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Teach) {
        this.eventManager.broadcast({ name: 'teachListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCourseById(index: number, item: Course) {
        return item.id;
    }

    trackInstructorById(index: number, item: Instructor) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-teach-popup',
    template: ''
})
export class TeachPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private teachPopupService: TeachPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.teachPopupService
                    .open(TeachDialogComponent as Component, params['id']);
            } else {
                this.teachPopupService
                    .open(TeachDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
