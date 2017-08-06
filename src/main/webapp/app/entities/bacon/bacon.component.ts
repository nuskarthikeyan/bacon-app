import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiAlertService } from 'ng-jhipster';

import { Bacon } from './bacon.model';
import { BaconService } from './bacon.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-bacon',
    templateUrl: './bacon.component.html'
})
export class BaconComponent implements OnInit, OnDestroy {
bacons: Bacon[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private baconService: BaconService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.baconService.query().subscribe(
            (res: ResponseWrapper) => {
                this.bacons = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInBacons();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Bacon) {
        return item.id;
    }
    registerChangeInBacons() {
        this.eventSubscriber = this.eventManager.subscribe('baconListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
