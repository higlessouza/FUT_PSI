import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPartida, Partida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';
import { PartidaComponent } from './partida.component';
import { PartidaDetailComponent } from './partida-detail.component';
import { PartidaUpdateComponent } from './partida-update.component';

@Injectable({ providedIn: 'root' })
export class PartidaResolve implements Resolve<IPartida> {
  constructor(private service: PartidaService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPartida> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((partida: HttpResponse<Partida>) => {
          if (partida.body) {
            return of(partida.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Partida());
  }
}

export const partidaRoute: Routes = [
  {
    path: '',
    component: PartidaComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.partida.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: PartidaDetailComponent,
    resolve: {
      partida: PartidaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.partida.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: PartidaUpdateComponent,
    resolve: {
      partida: PartidaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.partida.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: PartidaUpdateComponent,
    resolve: {
      partida: PartidaResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.partida.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
