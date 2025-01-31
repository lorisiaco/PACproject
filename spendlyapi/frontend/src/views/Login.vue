<!-- src/views/Login.vue -->
<template>
    <div class="login">
      <h2>Login</h2>
      <form @submit.prevent="handleLogin">
        <div>
          <label>Username</label>
          <input v-model="form.username" type="text" required />
        </div>
        <div>
          <label>Password</label>
          <input v-model="form.password" type="password" required />
        </div>
        <button type="submit">Accedi</button>
      </form>
      <div v-if="error">
        <p>{{ error }}</p>
      </div>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue'
  import { useAuthStore } from '../stores/auth'
  
  export default {
    name: 'Login',
    setup() {
      const auth = useAuthStore()
      const form = ref({
        username: '',
        password: ''
      })
      const error = ref(null)
  
      const handleLogin = async () => {
        try {
          await auth.login(form.value.username, form.value.password)
        } catch (err) {
          if (err.response && err.response.data) {
            error.value = err.response.data
          } else {
            error.value = 'Errore durante il login'
          }
        }
      }
  
      return { form, handleLogin, error }
    }
  }
  </script>
  
  <style scoped>
  /* Aggiungi il tuo stile qui */
  </style>
  