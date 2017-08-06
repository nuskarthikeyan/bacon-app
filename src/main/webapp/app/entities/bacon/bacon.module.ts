import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BaconAppSharedModule } from '../../shared';
import {
    BaconService,
    BaconPopupService,
    BaconComponent,
    BaconDetailComponent,
    BaconDialogComponent,
    BaconPopupComponent,
    BaconDeletePopupComponent,
    BaconDeleteDialogComponent,
    baconRoute,
    baconPopupRoute,
} from './';

const ENTITY_STATES = [
    ...baconRoute,
    ...baconPopupRoute,
];

@NgModule({
    imports: [
        BaconAppSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BaconComponent,
        BaconDetailComponent,
        BaconDialogComponent,
        BaconDeleteDialogComponent,
        BaconPopupComponent,
        BaconDeletePopupComponent,
    ],
    entryComponents: [
        BaconComponent,
        BaconDialogComponent,
        BaconPopupComponent,
        BaconDeleteDialogComponent,
        BaconDeletePopupComponent,
    ],
    providers: [
        BaconService,
        BaconPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BaconAppBaconModule {}
