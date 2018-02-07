import { BaseEntity } from './../../shared';

export class Instructor implements BaseEntity {
    constructor(
        public id?: number,
        public fname?: string,
        public lname?: string,
        public department?: BaseEntity,
    ) {
    }
}
