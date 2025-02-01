<template>
  <div class="register-page">
    <div class="register-container">
      <!-- Logo -->
      <img src="/images/logo.png" alt="Spendly Logo" class="logo" />

      <h2 class="register-title">Registrati su Spendly</h2>
      <hr />
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label for="firstName">Nome</label>
          <input
            id="firstName"
            v-model="form.firstName"
            type="text"
            class="form-control"
            placeholder="Inserisci il tuo nome"
            required
          />
        </div>
        <div class="form-group">
          <label for="lastName">Cognome</label>
          <input
            id="lastName"
            v-model="form.lastName"
            type="text"
            class="form-control"
            placeholder="Inserisci il tuo cognome"
            required
          />
        </div>
        <div class="form-group">
          <label for="username">Username</label>
          <input
            id="username"
            v-model="form.username"
            type="text"
            class="form-control"
            placeholder="Crea un username"
            required
          />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input
            id="email"
            v-model="form.email"
            type="email"
            class="form-control"
            placeholder="Inserisci la tua email"
            required
          />
        </div>
        <div class="form-group">
          <label for="phone">Telefono</label>
          <input
            id="phone"
            v-model="form.phone"
            type="text"
            class="form-control"
            placeholder="Inserisci il tuo numero di telefono"
          />
        </div>
        <div class="form-group">
          <label for="address">Indirizzo</label>
          <input
            id="address"
            v-model="form.address"
            type="text"
            class="form-control"
            placeholder="Inserisci il tuo indirizzo"
          />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input
            id="password"
            v-model="form.password"
            type="password"
            class="form-control"
            placeholder="Crea una password"
            required
            minlength="6"
          />
        </div>
        <div class="button-group">
          <button type="submit" class="btn btn-primary btn-register">Registrati</button>
          <button type="button" class="btn btn-outline-primary btn-cancel" @click="handleCancel">
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useAuthStore } from "../stores/auth";

export default {
  name: "Register",
  setup() {
    const router = useRouter();
    const auth = useAuthStore();
    const form = ref({
      firstName: "",
      lastName: "",
      username: "",
      email: "",
      phone: "",
      address: "",
      password: "",
    });

    const handleRegister = async () => {
      try {
        await auth.register(form.value);
      } catch (err) {
        console.error("Errore durante la registrazione:", err);
      }
    };

    const handleCancel = () => {
      router.push("/"); // Reindirizza alla homepage
    };

    return { form, handleRegister, handleCancel };
  },
};
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(to right, #4facfe, #00f2fe);
  padding: 2rem;
}

.register-container {
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 6px 30px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
  text-align: center;
}

.logo {
  width: 150px;
  margin-bottom: 1rem;
}

.register-title {
  font-size: 1.8rem;
  font-weight: bold;
  color: #1e3a8a;
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
  text-align: left;
}

label {
  font-size: 1rem;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 0.5rem;
  display: block;
}

.form-control {
  width: 100%;
  padding: 0.7rem;
  border-radius: 12px;
  border: 1px solid #d1d5db;
  font-size: 1rem;
  color: #374151;
  transition: border-color 0.3s ease;
}

.form-control:focus {
  border-color: #2563eb;
  outline: none;
}

.button-group {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
}

.btn-register {
  flex: 1;
  padding: 0.8rem;
  background-color: #2563eb;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-register:hover {
  background-color: #1e40af;
}

.btn-cancel {
  flex: 1;
  padding: 0.8rem;
  background-color: white;
  color: #2563eb;
  border: 1px solid #2563eb;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.btn-cancel:hover {
  background-color: #2563eb;
  color: white;
}

hr {
  border: none;
  border-top: 2px solid #e5e7eb;
  margin: 1rem 0;
}
</style>
