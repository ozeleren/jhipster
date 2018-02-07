import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Student } from './student.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StudentService {

    private resourceUrl =  SERVER_API_URL + 'api/students';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/students';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(student: Student): Observable<Student> {
        const copy = this.convert(student);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(student: Student): Observable<Student> {
        const copy = this.convert(student);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Student> {
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
     * Convert a returned JSON object to Student.
     */
    private convertItemFromServer(json: any): Student {
        const entity: Student = Object.assign(new Student(), json);
        entity.enrollmentDate = this.dateUtils
            .convertLocalDateFromServer(json.enrollmentDate);
        entity.birthDate = this.dateUtils
            .convertDateTimeFromServer(json.birthDate);
        entity.expectedGraduationDate = this.dateUtils
            .convertDateTimeFromServer(json.expectedGraduationDate);
        return entity;
    }

    /**
     * Convert a Student to a JSON which can be sent to the server.
     */
    private convert(student: Student): Student {
        const copy: Student = Object.assign({}, student);
        copy.enrollmentDate = this.dateUtils
            .convertLocalDateToServer(student.enrollmentDate);

        copy.birthDate = this.dateUtils.toDate(student.birthDate);

        copy.expectedGraduationDate = this.dateUtils.toDate(student.expectedGraduationDate);
        return copy;
    }
}
