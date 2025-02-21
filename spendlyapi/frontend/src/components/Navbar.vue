<template>
  <nav class="navbar">
    <div class="navbar-container">
      <!-- Logo con reindirizzamento -->
      <div class="navbar-logo">
        <router-link to="/dashboard">
          <img src="/images/logo.png" alt="Spendly Logo" class="logo" />
        </router-link>
      </div>
      <!-- Links dinamici -->
      <ul class="navbar-links">
        <template v-if="isAuthenticated">
          <li><router-link to="/groups">I Tuoi Gruppi</router-link></li>
          <li><router-link to="/gestione-spese">Le Tue Spese</router-link></li> <!-- Aggiungi il link a Gestione Spese -->
          <li><router-link to="/gestioneBudget">Il Tuo Budget</router-link></li> <!-- updated link -->
          <li><router-link to="/profile">Profilo</router-link></li>
        </template>
        <template v-else>
          <li><router-link to="/login">Login</router-link></li>
          <li><router-link to="/register">Registrati</router-link></li>
          <li><router-link to="/contact">Contatti</router-link></li>
        </template>
      </ul>
      <!-- Logout -->
      <button v-if="isAuthenticated" class="logout-button" @click="logout">
        Logout
      </button>
    </div>
  </nav>
</template>

<script>
import { computed } from "vue";
import { useAuthStore } from "../stores/auth";

export default {
  name: "Navbar",
  setup() {
    const auth = useAuthStore();
    const isAuthenticated = computed(() => !!auth.token); // Controlla se l'utente è loggato

    const logout = () => {
      auth.logout(); // Esegui il logout dallo store
    };

    return { isAuthenticated, logout };
  },
};
</script>

<style scoped>
/* Navbar generale */
.navbar {
  background-color: #121828; /* Colore scuro */
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
  cursor: pointer;
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
  color: #2cb1ea; /* Colore hover */
}

/* Bottone logout */
.logout-button {
  background-color: #d32f2f; /* Rosso */
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
  background-color: #b71c1c; /* Rosso scuro */
}
</style>
