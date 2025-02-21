import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Profile from '../views/Profile.vue';
import Admin from '../views/Admin.vue';
import Client from '../views/Client.vue';
import Dashboard from '../views/Dashboard.vue';
import Contact from '../views/Contact.vue';
import GestioneSpese from '../views/GestioneSpese.vue';
import GroupsView from '../views/GestioneGruppi.vue'; 
import GroupDetail from '../views/GroupDetail.vue';
import GestioneBudget from '../views/GestioneBudget.vue'; // <-- import del componente Budget

const routes = [
  // Rotta home
  { path: '/', name: 'Home', component: Home },

  // Rotte di autenticazione
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },

  // Esempio di rotta che richiede autenticazione
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requiresAuth: true },
  },

  // Rotta admin (richiede ruolo 'ROLE_admin')
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    meta: { requiresAuth: true, roles: ['ROLE_admin'] },
  },

  // Rotta client (richiede ruolo 'ROLE_client')
  {
    path: '/client',
    name: 'Client',
    component: Client,
    meta: { requiresAuth: true, roles: ['ROLE_client'] },
  },

  // Rotta generica per utenti loggati
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true },
  },

  // Contatti
  { path: '/contact', name: 'Contact', component: Contact },

  // Gestione spese
  {
    path: '/gestione-spese',
    name: 'GestioneSpese',
    component: GestioneSpese,
    meta: { requiresAuth: true },
  },

  // Gestione Budget
  {
    path: '/gestioneBudget',
    name: 'GestioneBudget',
    component: GestioneBudget,
    meta: { requiresAuth: true },
  },

  // Gestione gruppi
  {
    path: '/groups',
    name: 'Groups',
    component: GroupsView,
    meta: { requiresAuth: true },
  },
  {
    path: '/groups/:groupId',
    name: 'GroupDetail',
    component: GroupDetail,
    meta: { requiresAuth: true },
  },

  // Rotta di default se la pagina non esiste
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation Guard globale
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token'); // controlliamo se c'è un token
  const userRole = localStorage.getItem('role');         // eventualmente controlliamo anche il ruolo

  // Se la rotta richiede autenticazione ma l'utente non è autenticato
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Login' });
  }
  // Se la rotta richiede uno specifico ruolo e l'utente non lo ha
  else if (to.meta.roles && (!userRole || !to.meta.roles.includes(userRole))) {
    next({ name: 'Home' });
  }
  // Altrimenti va avanti
  else {
    next();
  }
});

export default router;
