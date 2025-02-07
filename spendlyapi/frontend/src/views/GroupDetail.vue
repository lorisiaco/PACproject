<template>
  <div class="group-detail-page">
    <!-- Contenitore principale con sfondo gradiente leggero -->
    <div
      class="container my-4 py-4 text-dark"
      style="
        max-width: 900px;
        background: linear-gradient(to bottom right, #f0f9ff, #cfe4fc);
        border-radius: 15px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      "
    >
      <!-- Immagine/banner in alto con titolo -->
      <div class="text-center mb-4">
        <!-- Utilizzo di un path assoluto a /images/banner.jpg -->
        <img
          src="/images/banner.jpg"
          alt="Banner Share"
          class="img-fluid rounded shadow-sm"
          style="max-height: 300px; object-fit: cover;"
        />
        <h2 class="mt-4 fw-bold" style="font-family: 'Comic Sans MS', cursive;">
          Dettaglio Gruppo
        </h2>
      </div>

      <!-- Card principale con bordo arrotondato e ombra -->
      <div class="card shadow-lg mb-5 border-0">
        <!-- Header della card con gradiente blu -->
        <div
          class="card-header text-white d-flex justify-content-between align-items-center"
          style="
            background: linear-gradient(to right, #007bff, #0056b3);
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
          "
        >
          <!-- Nome del gruppo -->
          <h4 class="mb-0">
            <i class="fas fa-users me-2"></i>{{ group.nome }}
          </h4>
          <!-- Badge con il numero dei membri -->
          <span
            class="badge bg-light text-primary fs-6 d-flex align-items-center"
            style="border-radius: 20px;"
          >
            <i class="fas fa-user-friends me-1"></i>
            {{ group.membri ? group.membri.length : 0 }} membri
          </span>
        </div>

        <!-- Corpo della card -->
        <div class="card-body position-relative">
          <!-- Se il gruppo ha membri: TABELLA IN ALTO -->
          <div v-if="group.membri && group.membri.length > 0" class="table-responsive mt-2">
            <h5 class="mb-3">
              <i class="fas fa-address-card me-1"></i> Membri del Gruppo
            </h5>
            <table class="table table-hover table-striped align-middle">
              <thead class="table-light">
                <tr>
                  <th class="text-center" style="width: 70%">Username</th>
                  <th class="text-center" style="width: 30%">Azione</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="member in group.membri" :key="member.id">
                  <td class="text-center">
                    <div class="d-flex align-items-center justify-content-center">
                      <!-- Nuova icona/avatar generica -->
                      <img
                        src="https://cdn-icons-png.flaticon.com/512/219/219986.png"
                        alt="Avatar"
                        style="width: 30px; margin-right: 8px;"
                      />
                      {{ member.username }}
                    </div>
                  </td>
                  <td class="text-center">
                    <button
                      class="btn btn-outline-danger btn-sm"
                      @click="removeMember(member.username)"
                    >
                      <i class="fas fa-user-minus"></i> Rimuovi
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Se non ci sono membri: messaggio -->
          <div v-else>
            <p class="text-muted mb-0">
              <i class="fas fa-exclamation-triangle"></i> Nessun membro presente in questo gruppo.
            </p>
          </div>

          <!-- Admin del gruppo, se presente -->
          <div class="mt-4" v-if="group.admin">
            <strong class="me-2">Admin:</strong>
            <span class="text-muted">{{ group.admin.username }}</span>
          </div>
        </div>

        <!-- Footer della card, con pulsante per tornare indietro -->
        <div class="card-footer d-flex justify-content-end border-0">
          <router-link to="/groups" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Torna alla lista gruppi
          </router-link>
        </div>
      </div>
    </div>

    <!-- FOOTER (come nel tuo esempio Home) -->
    <footer class="footer">
      <p>&copy; 2025 Spendly. Tutti i diritti riservati.</p>
      <router-link to="/contact" class="footer-link">Contattaci</router-link>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const groupId = route.params.groupId

// Stato del gruppo
const group = ref({})

// Al caricamento del componente, effettuiamo la GET /api/groups/:groupId
onMounted(() => {
  fetchGroup()
})

async function fetchGroup() {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      alert('Token mancante. Devi essere autenticato.')
      router.push({ name: 'Login' }) // Opzionale
      return
    }
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) {
      throw new Error('Errore nel recupero del gruppo.')
    }
    const data = await res.json()
    group.value = data
  } catch (error) {
    console.error(error)
    alert('Impossibile caricare il dettaglio del gruppo.')
  }
}

/**
 * Rimuove un membro dal gruppo
 * DELETE /api/groups/{groupId}/members?adminUsername=...&memberUsername=...
 */
async function removeMember(memberUsername) {
  try {
    const token = localStorage.getItem('token')
    const adminUsername = localStorage.getItem('username')

    if (!token || !adminUsername) {
      alert('Utente non autenticato o token mancante.')
      return
    }
    const url = `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`

    const response = await fetch(url, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!response.ok) {
      const errorMsg = await response.text()
      throw new Error(errorMsg)
    }

    // Ricarichiamo i dati del gruppo per aggiornare la lista
    fetchGroup()
  } catch (error) {
    alert('Errore nella rimozione del membro: ' + error.message)
    console.error('removeMember:', error)
  }
}
</script>

<style>
/* Manteniamo gli stili del dettaglio gruppo */
.group-detail-page .card {
  border-radius: 8px;
}

.group-detail-page .table-hover tbody tr:hover {
  background-color: #f1f9ff;
}

/* Stili del footer, copiati dal tuo esempio */
.footer {
  background: #1e293b;
  color: white;
  text-align: center;
  padding: 1.5rem 1rem;
}

.footer-link {
  color: #38bdf8;
  text-decoration: none;
  font-weight: bold;
}

.footer-link:hover {
  text-decoration: underline;
}
</style>
