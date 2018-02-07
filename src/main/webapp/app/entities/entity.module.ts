import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JhipsterDepartmentModule } from './department/department.module';
import { JhipsterStudentModule } from './student/student.module';
import { JhipsterCourseModule } from './course/course.module';
import { JhipsterTakeModule } from './take/take.module';
import { JhipsterInstructorModule } from './instructor/instructor.module';
import { JhipsterTeachModule } from './teach/teach.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JhipsterDepartmentModule,
        JhipsterStudentModule,
        JhipsterCourseModule,
        JhipsterTakeModule,
        JhipsterInstructorModule,
        JhipsterTeachModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterEntityModule {}
