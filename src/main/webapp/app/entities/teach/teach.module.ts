import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../../shared';
import {
    TeachService,
    TeachPopupService,
    TeachComponent,
    TeachDetailComponent,
    TeachDialogComponent,
    TeachPopupComponent,
    TeachDeletePopupComponent,
    TeachDeleteDialogComponent,
    teachRoute,
    teachPopupRoute,
    TeachResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...teachRoute,
    ...teachPopupRoute,
];

@NgModule({
    imports: [
        JhipsterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TeachComponent,
        TeachDetailComponent,
        TeachDialogComponent,
        TeachDeleteDialogComponent,
        TeachPopupComponent,
        TeachDeletePopupComponent,
    ],
    entryComponents: [
        TeachComponent,
        TeachDialogComponent,
        TeachPopupComponent,
        TeachDeleteDialogComponent,
        TeachDeletePopupComponent,
    ],
    providers: [
        TeachService,
        TeachPopupService,
        TeachResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTeachModule {}
