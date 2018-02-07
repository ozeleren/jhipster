import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../../shared';
import {
    TakeService,
    TakePopupService,
    TakeComponent,
    TakeDetailComponent,
    TakeDialogComponent,
    TakePopupComponent,
    TakeDeletePopupComponent,
    TakeDeleteDialogComponent,
    takeRoute,
    takePopupRoute,
    TakeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...takeRoute,
    ...takePopupRoute,
];

@NgModule({
    imports: [
        JhipsterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TakeComponent,
        TakeDetailComponent,
        TakeDialogComponent,
        TakeDeleteDialogComponent,
        TakePopupComponent,
        TakeDeletePopupComponent,
    ],
    entryComponents: [
        TakeComponent,
        TakeDialogComponent,
        TakePopupComponent,
        TakeDeleteDialogComponent,
        TakeDeletePopupComponent,
    ],
    providers: [
        TakeService,
        TakePopupService,
        TakeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterTakeModule {}
