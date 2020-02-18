import {Flow} from '@vaadin/flow-frontend/Flow';
import {Router} from '@vaadin/router';

import './global-styles';
import './main-layout';

import './views/dashboard/dashboard-view';

const {serverSideRoutes} = new Flow({
  imports: () => import('../target/frontend/generated-flow-imports')
});

const routes = [
  {
    path: '',
    component: 'main-layout',
    children: [
      {path: '', component: 'dashboard-view'},
      {path: 'Dashboard', component: 'dashboard-view'},

      // add more client-side routes here
      // {
      //    path: '/another-view',
      //    component: 'another-view',
      //    action: async () => { await import('./views/another-view') }
      // },
      {
		path: '/help',
		component: 'app-help',
		action: async () => { await import('./views/help/app-help'); }
	  },
	  {
		path: '/dummy',
		component: 'dummy-view',
		action: async () => { await import('./views/dummy/dummy-view'); }
	  },

      // fallback to server-side Flow routes if no client-side route matches
      ...serverSideRoutes
    ]
  }
];

const router = new Router(document.querySelector('#outlet'));
router.setRoutes(routes);
