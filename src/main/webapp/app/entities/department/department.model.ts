import { BaseEntity } from './../../shared';

export class Department implements BaseEntity {
    constructor(
        public id?: number,
        public dname?: string,
        public numberOfStudents?: number,
    ) {
    }
}
