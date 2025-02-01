<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- Logo -->
      <div class="navbar-logo">
        <img src="/images/logo.png" alt="Spendly Logo" class="logo" />
      </div>
      <!-- Links dinamici -->
      <ul class="navbar-links">
        <li v-if="isAuthenticated">
          <router-link to="/expenses">Le Tue Spese</router-link>
        </li>
        <li v-if="isAuthenticated">
          <router-link to="/groups">I Tuoi Gruppi</router-link>
        </li>
        <li v-if="isAuthenticated">
          <router-link to="/budget">Budget</router-link>
        </li>
        <li v-if="isAuthenticated">
          <router-link to="/alerts">Alert</router-link>
        </li>
        <li v-if="isAuthenticated">
          <router-link to="/profile">Profilo</router-link>
        </li>
        <li v-else>
          <router-link to="/login">Login</router-link>
        </li>
        <li v-else>
          <router-link to="/register">Registrati</router-link>
        </li>
        <li v-else>
          <router-link to="/contact">Contatti</router-link>
        </li>
      </ul>
      <!-- Logout o Nessuna azione -->
      <button v-if="isAuthenticated" class="logout-button" @click="handleLogout">
        Logout
      </button>
    </div>
  </nav>
</template>

<script>
import { computed } from "vue";
import { useRouter } from "vue-router"; // Per la navigazione
import { useAuthStore } from "../stores/auth"; // Store dell'autenticazione

export default {
  name: "Navbar",
  setup() {
    const router = useRouter();
    const auth = useAuthStore();

    // Computed property per verificare se l'utente è autenticato
    const isAuthenticated = computed(() => !!auth.user);

    const handleLogout = () => {
      auth.logout(); // Funzione di logout nello store
      router.push("/login"); // Reindirizza alla pagina di login
    };

    return {
      isAuthenticated,
      handleLogout,
    };
  },
};
</script>

<style scoped>
/* Navbar generale */
.navbar {
  background-color: #1e293b; /* Colore scuro */
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Contenitore navbar */
.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

/* Logo */
.navbar-logo .logo {
  height: 40px;
}

/* Link */
.navbar-links {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 1rem;
}

.navbar-links li {
  font-size: 1rem;
}

.navbar-links a {
  text-decoration: none;
  color: white;
  font-weight: 500;
  transition: color 0.3s ease;
}

.navbar-links a:hover {
  color: #38bdf8; /* Colore azzurro chiaro per hover */
}

/* Bottone logout */
.logout-button {
  background-color: #ef4444; /* Rosso per logout */
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  font-weight: 500;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-button:hover {
  background-color: #dc2626; /* Rosso scuro per hover */
}
</style>
