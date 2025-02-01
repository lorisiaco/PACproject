<template>
  <div class="profile-page">
    <!-- Navbar -->
    <header class="profile-header">
      <div class="logo-container">
        <img src="/images/logo.png" alt="Spendly Logo" class="logo" @click="goToDashboard" />
      </div>
      <button class="logout-button" @click="handleLogout">Logout</button>
    </header>

    <!-- Profilo Utente -->
    <section class="profile-container" v-if="!isLoading">
      <div v-if="user">
        <div class="profile-card">
          <!-- Immagine utente -->
          <img :src="user.avatar || defaultAvatar" alt="User Avatar" class="avatar" />
          <h2 class="user-name">{{ user.firstName }} {{ user.lastName }}</h2>
          <p class="user-role">{{ user.role }}</p>
          <p class="registered-date">Registrato il: {{ user.registeredAt || "Data non disponibile" }}</p>
        </div>

        <!-- Dettagli -->
        <div class="profile-details">
          <h3>Dettagli Utente</h3>
          <ul>
            <li><strong>Email:</strong> {{ user.email }}</li>
            <li><strong>Telefono:</strong> {{ user.phone || 'Non specificato' }}</li>
            <li><strong>Indirizzo:</strong> {{ user.address || 'Non specificato' }}</li>
            <li><strong>Ruolo:</strong> {{ user.role }}</li>
          </ul>
        </div>

        <!-- Biografia -->
        <div class="profile-bio">
          <h3>Biografia</h3>
          <p>{{ biography }}</p>
        </div>
      </div>
      <div v-else>
        <p>Nessun dato utente trovato.</p>
      </div>
    </section>

    <!-- Spinner durante il caricamento -->
    <div class="loading-container" v-else>
      <div class="spinner">
        <div class="spinner-circle"></div>
      </div>
      <p>Caricamento dati utente...</p>
    </div>
  </div>
</template>

<script>
import { onMounted, ref, computed } from "vue";
import { useAuthStore } from "../stores/auth";
import { useRouter } from "vue-router";

export default {
  name: "Profile",
  setup() {
    const router = useRouter();
    const auth = useAuthStore();
    const user = ref(null);
    const isLoading = ref(true);
    const defaultAvatar = "/images/default.webp";

    onMounted(async () => {
      try {
        await auth.fetchProfile();
        user.value = auth.user;
      } catch (error) {
        console.error("Errore nel caricamento del profilo:", error);
      } finally {
        isLoading.value = false;
      }
    });

    const biography = computed(() => {
      if (!user.value) return "";
      return `Ciao! Mi chiamo ${user.value.firstName} e sono un appassionato di Spendly. Amo tenere traccia delle mie finanze, collaborare con i miei gruppi di spesa e trovare strategie per ottimizzare il budget. Nel tempo libero, mi piace leggere libri di economia, esplorare nuove tecnologie e viaggiare per scoprire culture diverse. Spendly mi aiuta a gestire tutto questo in modo facile e intuitivo!`;
    });

    const handleLogout = () => {
      auth.logout();
      router.push("/");
    };

    const goToDashboard = () => {
      router.push("/dashboard");
    };

    return { user, isLoading, defaultAvatar, biography, handleLogout, goToDashboard };
  },
};
</script>

<style scoped>
/* Font moderni */
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap');

.profile-page {
  font-family: 'Poppins', sans-serif;
  color: #1f2937;
  background: #f9fafb;
  padding-bottom: 2rem;
}

/* Navbar */
.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background: linear-gradient(135deg, #6d28d9, #4f46e5);
  color: white;
}

.logo {
  height: 50px;
  cursor: pointer;
}

.logout-button {
  background-color: #ef4444;
  color: white;
  border: none;
  padding: 0.5rem 1.2rem;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-button:hover {
  background-color: #dc2626;
}

/* Profilo */
.profile-container {
  max-width: 900px;
  margin: 2rem auto;
  background: white;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.profile-card {
  text-align: center;
  margin-bottom: 2rem;
}

.avatar {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  margin-bottom: 1rem;
  object-fit: cover;
  border: 4px solid #4f46e5;
}

.user-name {
  font-size: 1.8rem;
  font-weight: 700;
  color: #4f46e5;
}

.user-role {
  font-size: 1rem;
  color: #6b7280;
}

.registered-date {
  font-size: 0.9rem;
  color: #9ca3af;
}

/* Dettagli */
.profile-details {
  margin-bottom: 2rem;
}

.profile-details h3 {
  font-size: 1.5rem;
  color: #4f46e5;
  margin-bottom: 1rem;
}

.profile-details ul {
  list-style: none;
  padding: 0;
}

.profile-details li {
  margin: 0.5rem 0;
  font-size: 1rem;
}

/* Biografia */
.profile-bio h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.profile-bio p {
  font-size: 1rem;
  line-height: 1.6;
  color: #4b5563;
}

/* Spinner */
.loading-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 50vh;
}

.spinner {
  width: 80px;
  height: 80px;
  position: relative;
  margin-bottom: 1rem;
}

.spinner-circle {
  width: 100%;
  height: 100%;
  border: 8px solid #e5e7eb;
  border-top: 8px solid #4f46e5;
  border-radius: 50%;
  animation: spin 1.5s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
