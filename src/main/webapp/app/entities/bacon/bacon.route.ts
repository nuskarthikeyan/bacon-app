import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BaconComponent } from './bacon.component';
import { BaconDetailComponent } from './bacon-detail.component';
import { BaconPopupComponent } from './bacon-dialog.component';
import { BaconDeletePopupComponent } from './bacon-delete-dialog.component';

export const baconRoute: Routes = [
    {
        path: 'bacon',
        component: BaconComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bacons'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bacon/:id',
        component: BaconDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bacons'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const baconPopupRoute: Routes = [
    {
        path: 'bacon-new',
        component: BaconPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bacons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bacon/:id/edit',
        component: BaconPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bacons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bacon/:id/delete',
        component: BaconDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bacons'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
