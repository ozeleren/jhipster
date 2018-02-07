import { BaseEntity } from './../../shared';

export class Take implements BaseEntity {
    constructor(
        public id?: number,
        public grade?: number,
        public student?: BaseEntity,
        public course?: BaseEntity,
    ) {
    }
}
