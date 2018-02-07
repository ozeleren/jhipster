import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Instructor } from './instructor.model';
import { InstructorService } from './instructor.service';

@Component({
    selector: 'jhi-instructor-detail',
    templateUrl: './instructor-detail.component.html'
})
export class InstructorDetailComponent implements OnInit, OnDestroy {

    instructor: Instructor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private instructorService: InstructorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInInstructors();
    }

    load(id) {
        this.instructorService.find(id).subscribe((instructor) => {
            this.instructor = instructor;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInInstructors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'instructorListModification',
            (response) => this.load(this.instructor.id)
        );
    }
}
