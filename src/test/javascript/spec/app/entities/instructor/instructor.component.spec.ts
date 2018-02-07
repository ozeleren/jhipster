/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { JhipsterTestModule } from '../../../test.module';
import { InstructorComponent } from '../../../../../../main/webapp/app/entities/instructor/instructor.component';
import { InstructorService } from '../../../../../../main/webapp/app/entities/instructor/instructor.service';
import { Instructor } from '../../../../../../main/webapp/app/entities/instructor/instructor.model';

describe('Component Tests', () => {

    describe('Instructor Management Component', () => {
        let comp: InstructorComponent;
        let fixture: ComponentFixture<InstructorComponent>;
        let service: InstructorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [InstructorComponent],
                providers: [
                    InstructorService
                ]
            })
            .overrideTemplate(InstructorComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InstructorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InstructorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Instructor(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.instructors[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
