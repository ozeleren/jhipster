import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TakeComponent } from './take.component';
import { TakeDetailComponent } from './take-detail.component';
import { TakePopupComponent } from './take-dialog.component';
import { TakeDeletePopupComponent } from './take-delete-dialog.component';

@Injectable()
export class TakeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const takeRoute: Routes = [
    {
        path: 'take',
        component: TakeComponent,
        resolve: {
            'pagingParams': TakeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.take.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'take/:id',
        component: TakeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.take.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const takePopupRoute: Routes = [
    {
        path: 'take-new',
        component: TakePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.take.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'take/:id/edit',
        component: TakePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.take.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'take/:id/delete',
        component: TakeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.take.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
