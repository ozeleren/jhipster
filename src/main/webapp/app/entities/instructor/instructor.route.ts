import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { InstructorComponent } from './instructor.component';
import { InstructorDetailComponent } from './instructor-detail.component';
import { InstructorPopupComponent } from './instructor-dialog.component';
import { InstructorDeletePopupComponent } from './instructor-delete-dialog.component';

@Injectable()
export class InstructorResolvePagingParams implements Resolve<any> {

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

export const instructorRoute: Routes = [
    {
        path: 'instructor',
        component: InstructorComponent,
        resolve: {
            'pagingParams': InstructorResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.instructor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'instructor/:id',
        component: InstructorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.instructor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const instructorPopupRoute: Routes = [
    {
        path: 'instructor-new',
        component: InstructorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.instructor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'instructor/:id/edit',
        component: InstructorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.instructor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'instructor/:id/delete',
        component: InstructorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jhipsterApp.instructor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
