import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'time',
        loadChildren: () => import('./time/time.module').then(m => m.FutPsiTimeModule)
      },
      {
        path: 'partida',
        loadChildren: () => import('./partida/partida.module').then(m => m.FutPsiPartidaModule)
      },
      {
        path: 'campeonato',
        loadChildren: () => import('./campeonato/campeonato.module').then(m => m.FutPsiCampeonatoModule)
      },
      {
        path: 'plataforma',
        loadChildren: () => import('./plataforma/plataforma.module').then(m => m.FutPsiPlataformaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class FutPsiEntityModule {}
