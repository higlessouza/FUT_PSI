import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FutPsiTestModule } from '../../../test.module';
import { CampeonatoUpdateComponent } from 'app/entities/campeonato/campeonato-update.component';
import { CampeonatoService } from 'app/entities/campeonato/campeonato.service';
import { Campeonato } from 'app/shared/model/campeonato.model';

describe('Component Tests', () => {
  describe('Campeonato Management Update Component', () => {
    let comp: CampeonatoUpdateComponent;
    let fixture: ComponentFixture<CampeonatoUpdateComponent>;
    let service: CampeonatoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutPsiTestModule],
        declarations: [CampeonatoUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CampeonatoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CampeonatoUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CampeonatoService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Campeonato(123);
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
        const entity = new Campeonato();
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
