<!-- src/components/Navbar.vue -->
<template>
    <nav>
      <router-link to="/">Home</router-link>
      <div v-if="!isAuthenticated">
        <router-link to="/login">Login</router-link>
        <router-link to="/register">Registrati</router-link>
      </div>
      <div v-else>
        <router-link to="/profile">Profilo</router-link>
        <router-link v-if="role === 'ROLE_admin'" to="/admin">Admin</router-link>
        <router-link v-if="role === 'ROLE_client'" to="/client">Client</router-link>
        <button @click="logout">Logout</button>
      </div>
    </nav>
  </template>
  
  <script>
  import { computed } from 'vue'
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Navbar',
    setup() {
      const auth = useAuthStore()
      const isAuthenticated = computed(() => !!auth.token)
      const role = computed(() => auth.role)
  
      const logout = () => {
        auth.logout()
      }
  
      return { isAuthenticated, role, logout }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  nav {
    display: flex;
    gap: 10px;
    padding: 10px;
    background-color: #f5f5f5;
  }
  </style>
  