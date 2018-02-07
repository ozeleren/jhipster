/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';

import { JhipsterTestModule } from '../../../test.module';
import { TeachDetailComponent } from '../../../../../../main/webapp/app/entities/teach/teach-detail.component';
import { TeachService } from '../../../../../../main/webapp/app/entities/teach/teach.service';
import { Teach } from '../../../../../../main/webapp/app/entities/teach/teach.model';

describe('Component Tests', () => {

    describe('Teach Management Detail Component', () => {
        let comp: TeachDetailComponent;
        let fixture: ComponentFixture<TeachDetailComponent>;
        let service: TeachService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [TeachDetailComponent],
                providers: [
                    TeachService
                ]
            })
            .overrideTemplate(TeachDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeachDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeachService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new Teach(123)));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.teach).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
