<!-- src/views/Client.vue -->
<template>
    <div class="client">
      <h2>Area Client</h2>
      <p>Benvenuto Cliente: {{ user.username }}</p>
      <!-- Aggiungi funzionalità specifiche per i client -->
    </div>
  </template>
  
  <script>
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Client',
    setup() {
      const auth = useAuthStore()
      return { user: auth.user }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  </style>
  