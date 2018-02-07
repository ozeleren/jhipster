/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { JhipsterTestModule } from '../../../test.module';
import { TeachComponent } from '../../../../../../main/webapp/app/entities/teach/teach.component';
import { TeachService } from '../../../../../../main/webapp/app/entities/teach/teach.service';
import { Teach } from '../../../../../../main/webapp/app/entities/teach/teach.model';

describe('Component Tests', () => {

    describe('Teach Management Component', () => {
        let comp: TeachComponent;
        let fixture: ComponentFixture<TeachComponent>;
        let service: TeachService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [TeachComponent],
                providers: [
                    TeachService
                ]
            })
            .overrideTemplate(TeachComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeachComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeachService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Teach(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.teaches[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
