import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutPsiTestModule } from '../../../test.module';
import { TimeUpdateComponent } from 'app/entities/time/time-update.component';
import { TimeService } from 'app/entities/time/time.service';
import { Time } from 'app/shared/model/time.model';

describe('Component Tests', () => {
  describe('Time Management Update Component', () => {
    let comp: TimeUpdateComponent;
    let fixture: ComponentFixture<TimeUpdateComponent>;
    let service: TimeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutPsiTestModule],
        declarations: [TimeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TimeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TimeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TimeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Time(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Time();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
