<!-- src/views/Register.vue -->
<template>
    <div class="register">
      <h2>Registrazione</h2>
      <form @submit.prevent="handleRegister">
        <div>
          <label>Nome</label>
          <input v-model="form.firstName" type="text" required />
        </div>
        <div>
          <label>Cognome</label>
          <input v-model="form.lastName" type="text" required />
        </div>
        <div>
          <label>Username</label>
          <input v-model="form.username" type="text" required />
        </div>
        <div>
          <label>Email</label>
          <input v-model="form.email" type="email" required />
        </div>
        <div>
          <label>Telefono</label>
          <input v-model="form.phone" type="text" />
        </div>
        <div>
          <label>Indirizzo</label>
          <input v-model="form.address" type="text" />
        </div>
        <div>
          <label>Password</label>
          <input v-model="form.password" type="password" required minlength="6" />
        </div>
        <button type="submit">Registrati</button>
      </form>
      <div v-if="errors">
        <ul>
          <li v-for="(msg, field) in errors" :key="field">{{ field }}: {{ msg }}</li>
        </ul>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue'
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Register',
    setup() {
      const auth = useAuthStore()
      const form = ref({
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        phone: '',
        address: '',
        password: '',
      })
      const errors = ref(null)
  
      const handleRegister = async () => {
        try {
          await auth.register(form.value)
        } catch (err) {
          if (err.response && err.response.data) {
            errors.value = err.response.data
          } else {
            errors.value = { general: 'Errore durante la registrazione' }
          }
        }
      }
  
      return { form, handleRegister, errors }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  </style>
  