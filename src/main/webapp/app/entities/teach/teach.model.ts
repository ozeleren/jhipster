import { BaseEntity } from './../../shared';

export class Teach implements BaseEntity {
    constructor(
        public id?: number,
        public course?: BaseEntity,
        public instructor?: BaseEntity,
    ) {
    }
}
