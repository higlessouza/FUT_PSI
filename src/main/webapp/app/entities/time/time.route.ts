import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITime, Time } from 'app/shared/model/time.model';
import { TimeService } from './time.service';
import { TimeComponent } from './time.component';
import { TimeDetailComponent } from './time-detail.component';
import { TimeUpdateComponent } from './time-update.component';

@Injectable({ providedIn: 'root' })
export class TimeResolve implements Resolve<ITime> {
  constructor(private service: TimeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITime> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((time: HttpResponse<Time>) => {
          if (time.body) {
            return of(time.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Time());
  }
}

export const timeRoute: Routes = [
  {
    path: '',
    component: TimeComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.time.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TimeDetailComponent,
    resolve: {
      time: TimeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.time.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TimeUpdateComponent,
    resolve: {
      time: TimeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.time.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TimeUpdateComponent,
    resolve: {
      time: TimeResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'futPsiApp.time.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
