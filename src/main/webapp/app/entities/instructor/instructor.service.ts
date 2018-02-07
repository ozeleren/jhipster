import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Instructor } from './instructor.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class InstructorService {

    private resourceUrl =  SERVER_API_URL + 'api/instructors';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/instructors';

    constructor(private http: Http) { }

    create(instructor: Instructor): Observable<Instructor> {
        const copy = this.convert(instructor);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(instructor: Instructor): Observable<Instructor> {
        const copy = this.convert(instructor);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Instructor> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Instructor.
     */
    private convertItemFromServer(json: any): Instructor {
        const entity: Instructor = Object.assign(new Instructor(), json);
        return entity;
    }

    /**
     * Convert a Instructor to a JSON which can be sent to the server.
     */
    private convert(instructor: Instructor): Instructor {
        const copy: Instructor = Object.assign({}, instructor);
        return copy;
    }
}
