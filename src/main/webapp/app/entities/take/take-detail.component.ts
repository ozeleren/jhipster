import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Take } from './take.model';
import { TakeService } from './take.service';

@Component({
    selector: 'jhi-take-detail',
    templateUrl: './take-detail.component.html'
})
export class TakeDetailComponent implements OnInit, OnDestroy {

    take: Take;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private takeService: TakeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTakes();
    }

    load(id) {
        this.takeService.find(id).subscribe((take) => {
            this.take = take;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTakes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'takeListModification',
            (response) => this.load(this.take.id)
        );
    }
}
