import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Profile from '../views/Profile.vue';
import Admin from '../views/Admin.vue';
import Client from '../views/Client.vue';
import Dashboard from '../views/Dashboard.vue'; // Importa il componente Dashboard
import Contact from '../views/Contact.vue'; // Importa il componente Contact
import GestioneSpese from '../views/GestioneSpese.vue'; // Importa il componente GestioneSpese
//import GestioneSpese from '../views/ExpensViw.vue'; // Importa il componente GestioneSpese

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/profile', name: 'Profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAuth: true, roles: ['ROLE_admin'] } },
  { path: '/client', name: 'Client', component: Client, meta: { requiresAuth: true, roles: ['ROLE_client'] } },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/contact', name: 'Contact', component: Contact },
  { path: '/gestione-spese', name: 'GestioneSpese', component: GestioneSpese, meta: { requiresAuth: true } },
  // Reindirizza tutte le rotte non definite alla homepage
  { path: '/:pathMatch(.*)*', redirect: '/' },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigazione protetta: verifica se l'utente è autenticato e se il ruolo corrisponde
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token');
  const userRole = localStorage.getItem('role');

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Se la rotta richiede autenticazione ma non c'è il token, reindirizza al Login
    next({ name: 'Login' });
  } else if (to.meta.roles && (!userRole || !to.meta.roles.includes(userRole))) {
    // Se la rotta richiede ruoli specifici e il ruolo dell'utente non è abilitato, reindirizza alla Home
    next({ name: 'Home' });
  } else {
    next();
  }
});

export default router;
