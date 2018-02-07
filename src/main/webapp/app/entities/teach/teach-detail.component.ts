import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Teach } from './teach.model';
import { TeachService } from './teach.service';

@Component({
    selector: 'jhi-teach-detail',
    templateUrl: './teach-detail.component.html'
})
export class TeachDetailComponent implements OnInit, OnDestroy {

    teach: Teach;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private teachService: TeachService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTeaches();
    }

    load(id) {
        this.teachService.find(id).subscribe((teach) => {
            this.teach = teach;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTeaches() {
        this.eventSubscriber = this.eventManager.subscribe(
            'teachListModification',
            (response) => this.load(this.teach.id)
        );
    }
}
