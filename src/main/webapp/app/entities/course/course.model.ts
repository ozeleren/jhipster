import { BaseEntity } from './../../shared';

export class Course implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public credits?: number,
        public department?: BaseEntity,
    ) {
    }
}
