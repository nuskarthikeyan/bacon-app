import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Bacon } from './bacon.model';
import { BaconPopupService } from './bacon-popup.service';
import { BaconService } from './bacon.service';

@Component({
    selector: 'jhi-bacon-dialog',
    templateUrl: './bacon-dialog.component.html'
})
export class BaconDialogComponent implements OnInit {

    bacon: Bacon;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private baconService: BaconService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.bacon.id !== undefined) {
            this.subscribeToSaveResponse(
                this.baconService.update(this.bacon));
        } else {
            this.subscribeToSaveResponse(
                this.baconService.create(this.bacon));
        }
    }

    private subscribeToSaveResponse(result: Observable<Bacon>) {
        result.subscribe((res: Bacon) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Bacon) {
        this.eventManager.broadcast({ name: 'baconListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-bacon-popup',
    template: ''
})
export class BaconPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private baconPopupService: BaconPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.baconPopupService
                    .open(BaconDialogComponent as Component, params['id']);
            } else {
                this.baconPopupService
                    .open(BaconDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
