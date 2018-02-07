import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Student } from './student.model';
import { StudentService } from './student.service';
import { Principal, ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student',
    templateUrl: './student.component.html'
})
export class StudentComponent implements OnInit, OnDestroy {
students: Student[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private studentService: StudentService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.studentService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.students = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.studentService.query().subscribe(
            (res: ResponseWrapper) => {
                this.students = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInStudents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Student) {
        return item.id;
    }
    registerChangeInStudents() {
        this.eventSubscriber = this.eventManager.subscribe('studentListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
