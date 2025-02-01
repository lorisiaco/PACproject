import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import Profile from '../views/Profile.vue';
import Admin from '../views/Admin.vue';
import Client from '../views/Client.vue';
import Dashboard from '../views/Dashboard.vue'; // Importa il componente Dashboard
import Contact from '../views/Contact.vue'; // Importa il componente Contact

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/profile', name: 'Profile', component: Profile, meta: { requiresAuth: true } },
  { path: '/admin', name: 'Admin', component: Admin, meta: { requiresAuth: true, roles: ['ROLE_admin'] } },
  { path: '/client', name: 'Client', component: Client, meta: { requiresAuth: true, roles: ['ROLE_client'] } },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/contact', name: 'Contact', component: Contact }, // Aggiunta della pagina contatti
  { path: '/:pathMatch(.*)*', redirect: '/' }, // Reindirizza tutte le rotte non definite alla homepage
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigazione protetta
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token');
  const userRole = localStorage.getItem('role');

  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Login' });
  } else if (to.meta.roles && !to.meta.roles.includes(userRole)) {
    next({ name: 'Home' });
  } else {
    next();
  }
});

export default router;
