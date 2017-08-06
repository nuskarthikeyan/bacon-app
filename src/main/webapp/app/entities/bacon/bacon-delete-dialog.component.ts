import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Bacon } from './bacon.model';
import { BaconPopupService } from './bacon-popup.service';
import { BaconService } from './bacon.service';

@Component({
    selector: 'jhi-bacon-delete-dialog',
    templateUrl: './bacon-delete-dialog.component.html'
})
export class BaconDeleteDialogComponent {

    bacon: Bacon;

    constructor(
        private baconService: BaconService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.baconService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'baconListModification',
                content: 'Deleted an bacon'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-bacon-delete-popup',
    template: ''
})
export class BaconDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private baconPopupService: BaconPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.baconPopupService
                .open(BaconDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
