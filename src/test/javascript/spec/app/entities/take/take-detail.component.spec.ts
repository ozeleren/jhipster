/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { JhipsterTestModule } from '../../../test.module';
import { TakeDetailComponent } from '../../../../../../main/webapp/app/entities/take/take-detail.component';
import { TakeService } from '../../../../../../main/webapp/app/entities/take/take.service';
import { Take } from '../../../../../../main/webapp/app/entities/take/take.model';

describe('Component Tests', () => {

    describe('Take Management Detail Component', () => {
        let comp: TakeDetailComponent;
        let fixture: ComponentFixture<TakeDetailComponent>;
        let service: TakeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [TakeDetailComponent],
                providers: [
                    TakeService
                ]
            })
            .overrideTemplate(TakeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TakeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TakeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Take(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.take).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
