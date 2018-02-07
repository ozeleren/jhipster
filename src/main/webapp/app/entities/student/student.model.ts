import { BaseEntity } from './../../shared';

export class Student implements BaseEntity {
    constructor(
        public id?: number,
        public fname?: string,
        public sname?: string,
        public gpa?: number,
        public enrollmentDate?: any,
        public birthDate?: any,
        public expectedGraduationDate?: any,
        public department?: BaseEntity,
    ) {
    }
}
