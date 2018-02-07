/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterTestModule } from '../../../test.module';
import { TeachDialogComponent } from '../../../../../../main/webapp/app/entities/teach/teach-dialog.component';
import { TeachService } from '../../../../../../main/webapp/app/entities/teach/teach.service';
import { Teach } from '../../../../../../main/webapp/app/entities/teach/teach.model';
import { CourseService } from '../../../../../../main/webapp/app/entities/course';
import { InstructorService } from '../../../../../../main/webapp/app/entities/instructor';

describe('Component Tests', () => {

    describe('Teach Management Dialog Component', () => {
        let comp: TeachDialogComponent;
        let fixture: ComponentFixture<TeachDialogComponent>;
        let service: TeachService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [TeachDialogComponent],
                providers: [
                    CourseService,
                    InstructorService,
                    TeachService
                ]
            })
            .overrideTemplate(TeachDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TeachDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TeachService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Teach(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.teach = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'teachListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Teach();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.teach = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'teachListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
