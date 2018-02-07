import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSharedModule } from '../../shared';
import {
    InstructorService,
    InstructorPopupService,
    InstructorComponent,
    InstructorDetailComponent,
    InstructorDialogComponent,
    InstructorPopupComponent,
    InstructorDeletePopupComponent,
    InstructorDeleteDialogComponent,
    instructorRoute,
    instructorPopupRoute,
    InstructorResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...instructorRoute,
    ...instructorPopupRoute,
];

@NgModule({
    imports: [
        JhipsterSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        InstructorComponent,
        InstructorDetailComponent,
        InstructorDialogComponent,
        InstructorDeleteDialogComponent,
        InstructorPopupComponent,
        InstructorDeletePopupComponent,
    ],
    entryComponents: [
        InstructorComponent,
        InstructorDialogComponent,
        InstructorPopupComponent,
        InstructorDeleteDialogComponent,
        InstructorDeletePopupComponent,
    ],
    providers: [
        InstructorService,
        InstructorPopupService,
        InstructorResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterInstructorModule {}
