import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TeachComponent } from './teach.component';
import { TeachDetailComponent } from './teach-detail.component';
import { TeachPopupComponent } from './teach-dialog.component';
import { TeachDeletePopupComponent } from './teach-delete-dialog.component';

@Injectable()
export class TeachResolvePagingParams implements Resolve<any> {

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

export const teachRoute: Routes = [
    {
        path: 'teach',
        component: TeachComponent,
        resolve: {
            'pagingParams': TeachResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.teach.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'teach/:id',
        component: TeachDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.teach.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const teachPopupRoute: Routes = [
    {
        path: 'teach-new',
        component: TeachPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.teach.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'teach/:id/edit',
        component: TeachPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.teach.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'teach/:id/delete',
        component: TeachDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.teach.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
