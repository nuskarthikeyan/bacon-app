import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Bacon } from './bacon.model';
import { BaconService } from './bacon.service';

@Component({
    selector: 'jhi-bacon-detail',
    templateUrl: './bacon-detail.component.html'
})
export class BaconDetailComponent implements OnInit, OnDestroy {

    bacon: Bacon;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private baconService: BaconService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInBacons();
    }

    load(id) {
        this.baconService.find(id).subscribe((bacon) => {
            this.bacon = bacon;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInBacons() {
        this.eventSubscriber = this.eventManager.subscribe(
            'baconListModification',
            (response) => this.load(this.bacon.id)
        );
    }
}
