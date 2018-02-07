/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterTestModule } from '../../../test.module';
import { InstructorDialogComponent } from '../../../../../../main/webapp/app/entities/instructor/instructor-dialog.component';
import { InstructorService } from '../../../../../../main/webapp/app/entities/instructor/instructor.service';
import { Instructor } from '../../../../../../main/webapp/app/entities/instructor/instructor.model';
import { DepartmentService } from '../../../../../../main/webapp/app/entities/department';

describe('Component Tests', () => {

    describe('Instructor Management Dialog Component', () => {
        let comp: InstructorDialogComponent;
        let fixture: ComponentFixture<InstructorDialogComponent>;
        let service: InstructorService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JhipsterTestModule],
                declarations: [InstructorDialogComponent],
                providers: [
                    DepartmentService,
                    InstructorService
                ]
            })
            .overrideTemplate(InstructorDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InstructorDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InstructorService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Instructor(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(entity));
                        comp.instructor = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'instructorListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new Instructor();
                        spyOn(service, 'create').and.returnValue(Observable.of(entity));
                        comp.instructor = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'instructorListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
