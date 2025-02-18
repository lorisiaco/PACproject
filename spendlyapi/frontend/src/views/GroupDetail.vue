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

      <!-- Sezione ALERT -->
      <div class="mt-4">
        <h5><i class="fas fa-bell me-1"></i> Alert del Gruppo</h5>
        <button class="btn btn-success" @click="openModal">
          <i class="fas fa-plus"></i> Aggiungi Alert
        </button>

        <ul v-if="alerts.length > 0" class="list-group">
          <li v-for="alert in alerts" :key="alert.id" class="list-group-item d-flex justify-content-between align-items-center">
            <span>
              <i class="fas fa-exclamation-circle text-danger me-2"></i>
              {{ alert.nome }} - Limite: €{{ alert.limite.toFixed(2) }}
            </span>
            <!-- Pulsante per eliminare l'alert -->
            <button class="btn btn-sm btn-danger" @click="deleteAlert(alert.id)">
              <i class="fas fa-trash"></i> Elimina
            </button>
          </li>
        </ul>
        <p v-else class="text-muted"><i class="fas fa-check-circle text-success"></i> Nessun alert presente.</p>

        <!-- Modale per aggiungere un nuovo Alert -->
        <div v-if="showModal" class="modal fade show d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.5);">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Aggiungi un nuovo Alert</h5>
                <button type="button" class="btn-close" @click="closeModal"></button>
              </div>
              <div class="modal-body">
                <div class="mb-3">
                  <label class="form-label">Nome Alert</label>
                  <input v-model="newAlert.nome" type="text" class="form-control" placeholder="Nome alert">
                </div>
                <div class="mb-3">
                  <label class="form-label">Importo Limite (€)</label>
                  <input v-model.number="newAlert.limite" type="number" class="form-control" placeholder="Importo limite">
                </div>
                <div class="mb-3">
                  <label class="form-label">ID Gruppo</label>
                  <input v-model="groupId" type="text" class="form-control" readonly>
                </div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" @click="closeModal">Chiudi</button>
                <button type="button" class="btn btn-success" @click="confirmAddAlert">Aggiungi Alert</button>
              </div>
            </div>
          </div>
        </div>
      </div>


      <!-- Sezione SPESE -->
      <div class="mt-4">
        <h5><i class="fas fa-wallet me-1"></i> Spese del Gruppo</h5>
        <table v-if="costs.length > 0" class="table table-striped">
          <thead class="table-light">
            <tr>
              <th>Importo</th>
              <th>Categoria</th>
              <th>Utente</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="cost in costs" :key="cost.costId">
              <td>€ {{ cost.importo.toFixed(2) }}</td>
              <td>{{ cost.tipologia.replace('_', ' ') }}</td>
              <td>{{ cost.user.username }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else class="text-muted"><i class="fas fa-money-bill-wave"></i> Nessuna spesa registrata.</p>
      </div>
      <!-- Sezione per aggiungere un membro -->
      <div class="mt-4">
        <h5><i class="fas fa-user-plus me-1"></i> Aggiungi un membro</h5>
        <div class="d-flex align-items-center">
          <select class="form-select" v-model="selectedMember" style="max-width: 250px;">
            <option value="" disabled>Seleziona un utente</option>
            <option v-for="user in allUsers" :key="user.id" :value="user.username">
              {{ user.username }}
            </option>
          </select>
          <button class="btn btn-primary ms-2" @click="addMemberToGroup(groupId, selectedMember)">
            Aggiungi
          </button>
        </div>
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
const showModal = ref(false);

// Stati
const group = ref({})
const costs = ref([])
const alerts = ref([])
const allUsers = ref([]) // Tutti gli utenti disponibili
const selectedMember = ref('') // Utente selezionato per aggiunta
const newAlert = ref({ nome: '', limite: 0 })

const adminUsername = localStorage.getItem('username')
const token = localStorage.getItem('token')




// Al caricamento
onMounted(() => {
  fetchGroup()
  fetchUsers() // Carichiamo gli utenti disponibili
})

function openModal() {
  showModal.value = true;
}

// Funzione per chiudere il modale
function closeModal() {
  showModal.value = false;
}

// Funzione per aggiungere un alert
async function confirmAddAlert() {
  // Chiamata alla funzione che aggiunge l'alert
  await addAlert();
  await fetchGroup();
  // Chiudi il modale dopo aver aggiunto l'alert
  closeModal();
}

// Recupera i dettagli del gruppo
async function fetchGroup() {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      alert('Token mancante. Devi essere autenticato.')
      router.push({ name: 'Login' })
      return
    }
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    if (!res.ok) {
      throw new Error('Errore nel recupero del gruppo.')
    }
    const data = await res.json()
    group.value = data.group
    costs.value = data.costs
    alerts.value = data.alerts
  } catch (error) {
    console.error(error)
    alert('Impossibile caricare il dettaglio del gruppo.')
  }
}

async function addAlert() {
  try {
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}/alerts?adminUsername=${adminUsername}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(newAlert.value)
    })

    if (!res.ok) {
      const errorMsg = await res.text()
      throw new Error(errorMsg)
    }

    await fetchGroup()
    newAlert.value = { nome: '', limite: 0 }
  } catch (error) {
    alert(`Errore nella creazione dell'alert: ${error.message}`)
    console.error('addAlert:', error)
  }
}

async function deleteAlert(alertId) {
  try {
    const res = await fetch(`http://localhost:8080/api/groups/${groupId}/alerts/${alertId}?adminUsername=${adminUsername}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token}` }
    })

    if (!res.ok) {
      const errorMsg = await res.text()
      throw new Error(errorMsg)
    }

    await fetchGroup()
  } catch (error) {
    alert(`Errore nell'eliminazione dell'alert: ${error.message}`)
    console.error('deleteAlert:', error)
  }
}

// Recupera tutti gli utenti disponibili per l'aggiunta
async function fetchUsers() {
  const token = localStorage.getItem("token");
  if (!token) {
    console.error("Token mancante. Non posso caricare gli utenti.");
    return;
  }

  try {
    const res = await fetch("http://localhost:8080/account/users", {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!res.ok) {
      throw new Error("Errore nel recupero degli utenti");
    }

    const data = await res.json();
    allUsers.value = data;
  } catch (err) {
    console.error("Errore fetchAllUsers:", err);
  }
}

// funzione per aggiungere membri al gruppo
async function addMemberToGroup(groupId, memberUsername) {
  const adminUsername = localStorage.getItem("username");
  const token = localStorage.getItem("token");
  memberUsername = selectedMember.value; // Prendiamo il valore selezionato

  if (!adminUsername || !token) {
    alert("Utente non autenticato o token mancante.");
    return;
  }

  if (!memberUsername) {
    alert("Seleziona l'utente dal menu a tendina.");
    return;
  }
  try {
    const response = await fetch(
      `http://localhost:8080/api/groups/${groupId}/members?adminUsername=${adminUsername}&memberUsername=${memberUsername}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText);
    }

    // Ricarichiamo i gruppi per vedere il membro aggiunto
    fetchGroup();

    // Resettiamo la select
    selectedMember.value = "";
  } catch (error) {
    alert(`Errore aggiunta membro: ${error}`);
    console.error("Errore durante l'aggiunta del membro:", error);
  }
}

//funzione per rimuovere i membri dal gruppo
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
