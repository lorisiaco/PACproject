<!-- src/views/Admin.vue -->
<template>
    <div class="admin">
      <h2>Area Admin</h2>
      <p>Benvenuto Admin: {{ user.username }}</p>
      <!-- Aggiungi funzionalità specifiche per gli admin -->
    </div>
  </template>
  
  <script>
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Admin',
    setup() {
      const auth = useAuthStore()
      return { user: auth.user }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  </style>
  