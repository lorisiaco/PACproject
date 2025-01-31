<!-- src/views/Profile.vue -->
<template>
    <div class="profile">
      <h2>Profilo Utente</h2>
      <div v-if="user">
        <p><strong>Nome:</strong> {{ user.firstName }}</p>
        <p><strong>Cognome:</strong> {{ user.lastName }}</p>
        <p><strong>Username:</strong> {{ user.username }}</p>
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>Telefono:</strong> {{ user.phone }}</p>
        <p><strong>Indirizzo:</strong> {{ user.address }}</p>
        <p><strong>Ruolo:</strong> {{ user.role }}</p>
        <p><strong>Creato il:</strong> {{ user.createdAt }}</p>
      </div>
      <div v-else>
        <p>Caricamento...</p>
      </div>
    </div>
  </template>
  
  <script>
  import { onMounted } from 'vue'
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Profile',
    setup() {
      const auth = useAuthStore()
  
      onMounted(() => {
        auth.fetchProfile()
      })
  
      return { user: auth.user }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  </style>
  