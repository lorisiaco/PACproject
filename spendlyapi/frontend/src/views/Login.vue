<template>
  <div class="login-wrapper">
    <div class="login-container">
      <img src="/images/logo.png" alt="Spendly Logo" class="logo" />
      <h2 class="login-title">Welcome to Spendly</h2>
      <hr />

      <!-- Messaggio di errore -->
      <div v-if="error" class="alert alert-danger alert-dismissible fade show" role="alert">
        <strong>{{ error }}</strong>
        <button type="button" class="btn-close" @click="clearError"></button>
      </div>

      <!-- Form di login -->
      <form @submit.prevent="handleLogin">
        <div class="mb-3">
          <label for="username" class="form-label">Username</label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            class="form-control"
            placeholder="Enter your username"
            required
          />
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Password</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            class="form-control"
            placeholder="Enter your password"
            required
          />
        </div>
        <div class="row mb-3">
          <div class="col d-grid">
            <button type="submit" class="btn btn-primary btn-custom">Login</button>
          </div>
          <div class="col d-grid">
            <button type="button" class="btn btn-outline-primary btn-custom" @click="handleCancel">
              Cancel
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useAuthStore } from "../stores/auth"; // Importa lo store per la gestione del login

export default {
  name: "Login",
  setup() {
    const auth = useAuthStore(); // Store di autenticazione
    const form = ref({
      username: "",
      password: "",
    });
    const error = ref(null);

    const handleLogin = async () => {
      try {
        // Effettua il login tramite lo store e ottieni il token
        const token = await auth.login(form.value.username, form.value.password);
        // Salva il token e lo username in localStorage
        if (token) {
          localStorage.setItem('token', token);
        }
        localStorage.setItem('username', form.value.username);
        window.location.href = "/dashboard";
      } catch (err) {
        // Gestione degli errori
        if (err.response && err.response.data) {
          error.value = err.response.data.message || "Errore durante il login";
        } else {
          error.value = "Errore durante il login";
        }
      }
    };

    const handleCancel = () => {
      form.value.username = "";
      form.value.password = "";
      error.value = null;
    };

    const clearError = () => {
      error.value = null;
    };

    return {
      form,
      error,
      handleLogin,
      handleCancel,
      clearError,
    };
  },
};
</script>

<style scoped>
/* Wrapper per sfondo e centramento */
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #4facfe, #00f2fe); /* Gradiente azzurro */
  padding: 1rem;
  box-sizing: border-box;
}

/* Contenitore del form */
.login-container {
  background: white;
  padding: 2rem;
  border-radius: 16px; /* Bordi più arrotondati */
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15); /* Ombra più morbida */
  width: 100%;
  max-width: 400px;
  text-align: center;
}

/* Stile per il titolo */
.login-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #007bff;
  margin-bottom: 1rem;
}

/* Pulsanti */
.btn-custom {
  transition: background-color 0.3s ease;
}

.btn-custom:hover {
  background: #0056b3;
  color: white;
}

/* Input */
.form-control {
  border-radius: 8px;
  font-size: 1rem;
}

/* Messaggio di errore */
.alert-dismissible {
  font-size: 0.9rem;
}

/* Logo */
.logo {
  display: block;
  margin: 0 auto 1rem;
  width: 200px; /* Dimensione del logo */
}
</style>
