/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { Headers } from '@angular/http';

import { JhipsterTestModule } from '../../../test.module';
import { TakeComponent } from '../../../../../../main/webapp/app/entities/take/take.component';
import { TakeService } from '../../../../../../main/webapp/app/entities/take/take.service';
import { Take } from '../../../../../../main/webapp/app/entities/take/take.model';

describe('Component Tests', () => {

    describe('Take Management Component', () => {
        let comp: TakeComponent;
        let fixture: ComponentFixture<TakeComponent>;
        let service: TakeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [TakeComponent],
                providers: [
                    TakeService
                ]
            })
            .overrideTemplate(TakeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TakeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TakeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new Headers();
                headers.append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of({
                    json: [new Take(123)],
                    headers
                }));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.takes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
