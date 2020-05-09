import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FutPsiTestModule } from '../../../test.module';
import { CampeonatoDetailComponent } from 'app/entities/campeonato/campeonato-detail.component';
import { Campeonato } from 'app/shared/model/campeonato.model';

describe('Component Tests', () => {
  describe('Campeonato Management Detail Component', () => {
    let comp: CampeonatoDetailComponent;
    let fixture: ComponentFixture<CampeonatoDetailComponent>;
    const route = ({ data: of({ campeonato: new Campeonato(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FutPsiTestModule],
        declarations: [CampeonatoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CampeonatoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CampeonatoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load campeonato on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.campeonato).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
