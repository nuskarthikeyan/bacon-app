import { BaseEntity } from './../../shared';

export class Bacon implements BaseEntity {
    constructor(
        public id?: number,
        public brand?: string,
    ) {
    }
}
