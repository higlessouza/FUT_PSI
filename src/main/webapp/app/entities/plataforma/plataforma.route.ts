import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPlataforma, Plataforma } from 'app/shared/model/plataforma.model';
import { PlataformaService } from './plataforma.service';
import { PlataformaComponent } from './plataforma.component';
import { PlataformaDetailComponent } from './plataforma-detail.component';
import { PlataformaUpdateComponent } from './plataforma-update.component';

@Injectable({ providedIn: 'root' })
export class PlataformaResolve implements Resolve<IPlataforma> {
  constructor(private service: PlataformaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlataforma> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((plataforma: HttpResponse<Plataforma>) => {
          if (plataforma.body) {
            return of(plataforma.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Plataforma());
  }
}

export const plataformaRoute: Routes = [
  {
    path: '',
    component: PlataformaComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.plataforma.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PlataformaDetailComponent,
    resolve: {
      plataforma: PlataformaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.plataforma.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PlataformaUpdateComponent,
    resolve: {
      plataforma: PlataformaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.plataforma.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PlataformaUpdateComponent,
    resolve: {
      plataforma: PlataformaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.plataforma.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
